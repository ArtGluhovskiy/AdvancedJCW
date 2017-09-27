package org.art.dao.impl;

import org.art.dao.JavaTaskDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.entities.JavaTask;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.art.dao.util.DbcpConnectionPool.close;

/**
 * JavaTaskDao implementation
 */
public class JavaTaskDaoImpl implements JavaTaskDao {

    private static volatile JavaTaskDao instance = null;
    private ThreadLocal<Connection> threadCache;
    //Path of the file where serialized java task is stored
    private String serialTaskPath = "C:\\Users\\Lenovo\\IdeaProjects\\jsw_project\\src\\com\\java_projects\\java_code_wars\\source\\java_tasks\\serial_tasks\\Serial_task.txt";
    public static final Logger log = Logger.getLogger(UserDaoImpl.class);

    private static final String SAVE_JAVA_TASK_QUERY = "INSERT INTO java_tasks (difficulty_group, short_desc, elapsed_time, popularity," +
                                                       " java_task_object, reg_date) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String GET_JAVA_TASK_QUERY = "SELECT * FROM java_tasks WHERE task_id = ?";
    private static final String GET_TOP_JAVA_TASK_QUERY = "SELECT * FROM java_tasks ORDER BY popularity DESC LIMIT ?;";
    private static final String GET_TASK_BY_DIFF_GROUP = "SELECT * FROM java_tasks WHERE difficulty_group = ?" +
                                                         " AND task_id > ? ORDER BY task_id LIMIT 1;";
    private static final String GET_ALL_JAVA_TASK = "SELECT * FROM java_tasks";
    private static final String UPDATE_JAVA_TASK_QUERY = "UPDATE java_tasks SET difficulty_group = ?, short_desc = ?, elapsed_time = ?," +
                                                         " popularity = ? WHERE task_id = ?";
    private static final String DELETE_JAVA_TASK_QUERY = "DELETE FROM java_tasks WHERE task_id = ?";

    /**
     * This is an implementation of "Thread Scope" approach.
     * It means that every thread while transaction execution has its own connection
     * which contains in thread's cache (@code ThreadLocal).
     *
     * @param threadCache thread's cache which contains its own connection for transaction
     */
    public void setThreadCache(ThreadLocal<Connection> threadCache) {
        this.threadCache = threadCache;
    }

    private JavaTaskDaoImpl() {
    }

    public static JavaTaskDao getInstance() {
        JavaTaskDao javaTaskDao = instance;
        if (javaTaskDao == null) {
            synchronized (JavaTaskDaoImpl.class) {
                javaTaskDao = instance;
                if (javaTaskDao == null) {
                    instance = javaTaskDao = new JavaTaskDaoImpl();
                }
            }
        }
        return javaTaskDao;
    }

    @Override
    public JavaTask save(JavaTask javaTask) throws DAOSystemException {
        PreparedStatement psSave = null;
        Connection conn = threadCache.get();
        FileInputStream in = null;
        ResultSet rs = null;
        try {
            serializeTask(javaTask);
            in = new FileInputStream(new File(serialTaskPath));
            psSave = conn.prepareStatement(SAVE_JAVA_TASK_QUERY, Statement.RETURN_GENERATED_KEYS);
            psSave.setString(1, javaTask.getDifficultyGroup());
            psSave.setString(2, javaTask.getShortDescr());
            psSave.setLong(3, javaTask.getElapsedTime());
            psSave.setInt(4, javaTask.getPopularity());
            psSave.setBinaryStream(5, in);
            psSave.setDate(6, javaTask.getRegDate());
            psSave.executeUpdate();
            in.close();
            rs = psSave.getGeneratedKeys();
            if (rs.next()) {
                javaTask.setTaskID(rs.getLong(1));
            }
        } catch (IOException | SQLException e) {
            log.info("Cannot save task into database!", e);
            throw new DAOSystemException("Cannot save task into database!", e);
        } finally {
            close(rs);
            close(psSave);
            try {
                in.close();
            } catch (IOException e) {
                log.info("Exception while closing file", e);
                throw new DAOSystemException("Exception while closing file", e);
            }
        }
        return javaTask;
    }

    @Override
    public JavaTask get(long id) throws DAOSystemException {
        PreparedStatement psGet = null;
        JavaTask javaTask = null;
        ResultSet rs = null;
        Connection conn = threadCache.get();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            psGet = conn.prepareStatement(GET_JAVA_TASK_QUERY);
            psGet.setLong(1, id);
            rs = psGet.executeQuery();
            while (rs.next()) {
                javaTask = readTaskFromDB(rs, out);
            }
        } catch (ClassNotFoundException | IOException | SQLException e) {
            log.info("Can not get task from the database!", e);
            throw new DAOSystemException("Can not get task from the database!", e);
        } finally {
            close(rs);
            close(psGet);
        }
        return javaTask;
    }

    /**
     * This method deserializes java task from ByteArrayOutputStream into JavaTask instance
     *
     * @param out1 ByteArrayOutputStream from which the JavaTask instance is read
     * @return JavaTask which is read from byte stream
     * @throws IOException            in case of IO problems (file not found etc.) while task deserializing
     * @throws ClassNotFoundException if appropriate class was not found while task deserializing
     */
    private JavaTask deserializeTask(ByteArrayOutputStream out1) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(out1.toByteArray()));
        return (JavaTask) in.readObject();
    }

    /**
     * This method reads java task binary file from the DB table (in particular, from
     * BLOB column of "java_tasks" table) into ByteArrayOutputStream (out)
     *
     * @param rs  ResultSet of the query
     * @param out ByteArrayOutputStream into which the BLOB file is written
     * @return JavaTask from the database with appropriate parameters
     * @throws SQLException           in case of problems while task reading from the database
     * @throws IOException            in case of IO problems (file not found etc.) while task deserializing
     * @throws ClassNotFoundException if appropriate class was not found while task deserializing
     */
    private JavaTask readTaskFromDB(ResultSet rs, ByteArrayOutputStream out) throws SQLException, IOException, ClassNotFoundException {
        JavaTask javaTask;
        int bt;
        InputStream in = rs.getBinaryStream("java_task_object");
        while ((bt = in.read()) != -1) {
            out.write(bt);
        }
        javaTask = deserializeTask(out);
        javaTask.setTaskID(rs.getInt("task_id"));
        javaTask.setPopularity(rs.getInt("popularity"));
        javaTask.setDifficultyGroup(rs.getString("difficulty_group"));
        javaTask.setShortDescr(rs.getString("short_desc"));
        javaTask.setElapsedTime(rs.getInt("elapsed_time"));
        javaTask.setRegDate(rs.getDate("reg_date"));
        return javaTask;
    }

    @Override
    public int update(JavaTask javaTask) throws DAOSystemException {
        PreparedStatement psUpdate = null;
        Connection conn = threadCache.get();
        int amount;
        try {
            psUpdate = conn.prepareStatement(UPDATE_JAVA_TASK_QUERY);
            psUpdate.setString(1, javaTask.getDifficultyGroup());
            psUpdate.setString(2, javaTask.getShortDescr());
            psUpdate.setLong(3, javaTask.getElapsedTime());
            psUpdate.setInt(4, javaTask.getPopularity());
            psUpdate.setLong(5, javaTask.getTaskID());
            amount = psUpdate.executeUpdate();
        } catch (SQLException e) {
            log.info("Cannot update task in database!", e);
            throw new DAOSystemException("Cannot update task in database!", e);
        } finally {
            close(psUpdate);
        }
        return amount;
    }

    @Override
    public int delete(long id) throws DAOSystemException {
        PreparedStatement psDelete = null;
        int dr;
        Connection conn = threadCache.get();
        try {
            psDelete = conn.prepareStatement(DELETE_JAVA_TASK_QUERY);
            psDelete.setLong(1, id);
            dr = psDelete.executeUpdate();
        } catch (SQLException e) {
            log.info("Cannot delete task in database!", e);
            throw new DAOSystemException("Cannot delete task in database!", e);
        } finally {
            close(psDelete);
        }
        return dr;
    }

    @Override
    public JavaTask getNextTaskByDiffGroup(String difGroup, long taskID) throws DAOSystemException {
        PreparedStatement psNextTask = null;
        JavaTask javaTask = null;
        ResultSet rs = null;
        Connection conn = threadCache.get();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            psNextTask = conn.prepareStatement(GET_TASK_BY_DIFF_GROUP);
            psNextTask.setString(1, difGroup);
            psNextTask.setLong(2, taskID);
            rs = psNextTask.executeQuery();
            while (rs.next()) {
                javaTask = readTaskFromDB(rs, out);
            }
        } catch (ClassNotFoundException | IOException | SQLException e) {
            log.info("Cannot get task from database!", e);
            throw new DAOSystemException("Cannot get task from database!", e);
        } finally {
            close(rs);
            close(psNextTask);
        }
        return javaTask;
    }

    @Override
    public List<JavaTask> getPopularJavaTasks(int taskAmount) throws DAOSystemException {
        PreparedStatement psGetPopTask = null;
        List<JavaTask> taskList = new ArrayList<>();
        JavaTask javaTask;
        ResultSet rs = null;
        Connection conn = threadCache.get();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            psGetPopTask = conn.prepareStatement(GET_TOP_JAVA_TASK_QUERY);
            psGetPopTask.setInt(1, taskAmount);
            rs = psGetPopTask.executeQuery();
            while (rs.next()) {
                javaTask = readTaskFromDB(rs, out);
                taskList.add(javaTask);
            }
        } catch (ClassNotFoundException | IOException | SQLException e) {
            log.info("Cannot get task from database!", e);
            throw new DAOSystemException("Cannot get task from database!", e);
        } finally {
            close(rs);
            close(psGetPopTask);
        }
        return taskList;
    }

    @Override
    public List<JavaTask> getAll() throws DAOSystemException {
        PreparedStatement psGetAll = null;
        List<JavaTask> taskList = new ArrayList<>();
        JavaTask javaTask;
        ResultSet rs = null;
        Connection conn = threadCache.get();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            psGetAll = conn.prepareStatement(GET_ALL_JAVA_TASK);
            rs = psGetAll.executeQuery();
            while (rs.next()) {
                javaTask = readTaskFromDB(rs, out);
                taskList.add(javaTask);
            }
        } catch (ClassNotFoundException | IOException | SQLException e) {
            log.info("Cannot get tasks from database!", e);
            throw new DAOSystemException("Cannot get tasks from database!", e);
        } finally {
            close(rs);
            close(psGetAll);
        }
        return taskList;
    }

    @Override
    public void createJavaTasksTable() throws DAOSystemException {
        Connection conn = threadCache.get();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            StringBuilder sb = new StringBuilder("CREATE TABLE java_tasks (")
                    .append("task_id INT(11) AUTO_INCREMENT PRIMARY KEY,")
                    .append("difficulty_group ENUM('BEGINNER', 'EXPERIENCED', 'EXPERT'),")
                    .append("short_desc VARCHAR(255),")
                    .append("elapsed_time INT(11),")
                    .append("popularity INT DEFAULT 0,")
                    .append("java_task_object LONGBLOB,")
                    .append("reg_date DATE);");
            stmt.execute(sb.toString());
        } catch (SQLException e) {
            log.info("Cannot create tasks table in database!", e);
            throw new DAOSystemException("Cannot create tasks table in database!", e);
        } finally {
            close(stmt);
        }
    }

    @Override
    public void deleteJavaTasksTable() throws DAOSystemException {
        Connection conn = threadCache.get();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE java_tasks;");
        } catch (SQLException e) {
            log.info("Cannot delete tasks table in database!", e);
            throw new DAOSystemException("Cannot delete tasks table in database!", e);
        } finally {
            close(stmt);
        }
    }

    /**
     * This method serializes java task into file before its saving to the database
     *
     * @param task task for serializing
     * @throws DAOSystemException in case of IO problems (file not found etc.) during task serializing
     */
    public static void serializeTask(JavaTask task) throws DAOSystemException {
        String filePath = "C:\\Users\\Lenovo\\IdeaProjects\\jsw_project\\src\\com\\java_projects\\java_code_wars\\source\\java_tasks\\serial_tasks\\Serial_task.txt";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(filePath)))) {
            out.writeObject(task);
        } catch (FileNotFoundException e) {
            throw new DAOSystemException("Can not find file to write JavaTask!", e);
        } catch (IOException e1) {
            throw new DAOSystemException("IO Exception while serializing JavaTask!", e1);
        }
    }
}
