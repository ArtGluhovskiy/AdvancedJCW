package org.art.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.JavaTaskDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.repository.JavaTaskRepository;
import org.art.entities.JavaTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaTaskDao implementation
 */
public class JavaTaskDaoImpl implements JavaTaskDao {

    private JavaTaskRepository taskRepository;

    public static final Logger log = LogManager.getLogger(JavaTaskDaoImpl.class);

    @Autowired
    public void setJavaTaskRepository(JavaTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //Path of the file where serialized java task is stored
    private String serialTaskPath = "C:\\Users\\admin1\\IdeaProjects\\AdvancedJCW\\dao\\src\\main\\resources\\files\\Serial_task.txt";


    @Override
    public JavaTask save(JavaTask javaTask) throws DAOSystemException {
        JavaTask savedTask;
        try {
            serializeTask(javaTask);
            Path path = Paths.get(serialTaskPath);
            byte[] data = Files.readAllBytes(path);
            javaTask.setBinTask(data);
            savedTask = taskRepository.save(javaTask);
        } catch (Exception e) {
            log.info("Cannot save task into database!", e);
            throw new DAOSystemException("Cannot save task into database!", e);
        }
        return savedTask;
    }

    @Override
    public JavaTask get(Long id) throws DAOSystemException {
        JavaTask serialTask;
        JavaTask deserialTask;
        try {
            serialTask = taskRepository.findById(id).orElse(null);
            if (serialTask != null) {
                deserialTask = deserializeTask(serialTask.getBinTask());
                deserialTask.setTaskId(serialTask.getTaskId());
                return deserialTask;
            }
        } catch (Exception e) {
            log.info("Can not get task from the database!", e);
            throw new DAOSystemException("Can not get task from the database!", e);
        }
        return null;
    }

    /**
     * This method deserializes java task from ByteArrayOutputStream into JavaTask instance
     *
     * @param data byte array, from which the JavaTask instance is read
     * @return JavaTask which is read from byte stream
     * @throws IOException            in case of IO problems (file not found etc.) while task deserializing
     * @throws ClassNotFoundException if appropriate class was not found while task deserializing
     */
    private JavaTask deserializeTask(byte[] data) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return (JavaTask) in.readObject();
    }

    @Override
    public JavaTask update(JavaTask javaTask) throws DAOSystemException {
        JavaTask updTask;
        try {
            updTask = taskRepository.save(javaTask);
        } catch (Exception e) {
            log.info("Cannot update task in the database!", e);
            throw new DAOSystemException("Cannot update task in the database!", e);
        }
        return updTask;
    }

    @Override
    public void delete(Long id) throws DAOSystemException {
        try {
            taskRepository.deleteById(id);
        } catch (Exception e) {
            log.info("Cannot delete task from the database!", e);
            throw new DAOSystemException("Cannot delete task from the database!", e);
        }
    }

    @Override
    public JavaTask getNextTaskByDiffGroup(String difGroup, Long taskID) throws DAOSystemException {
        JavaTask serialTask;
        JavaTask deserialTask;
        try {
            serialTask = taskRepository.findFirstByDifficultyGroupAndTaskIdGreaterThan(difGroup, taskID);
            if (serialTask != null) {
                deserialTask = deserializeTask(serialTask.getBinTask());
                deserialTask.setTaskId(serialTask.getTaskId());
                return deserialTask;
            }
        } catch (Exception e) {
            log.info("Cannot get task from database!", e);
            throw new DAOSystemException("Cannot get task from database!", e);
        }
        return null;
    }

    @Override
    public List<JavaTask> getPopularJavaTasks(int taskAmount) throws DAOSystemException {
        List<JavaTask> taskList;
        try {
            taskList = taskRepository.findAll(PageRequest.of(0, taskAmount, Sort.Direction.DESC, "popularity")).getContent();
        } catch (Exception e) {
            log.info("Cannot get tasks from the database!", e);
            throw new DAOSystemException("Cannot get task from the database!", e);
        }
        return taskList;
    }

    @Override
    public List<JavaTask> getAll() throws DAOSystemException {
        List<JavaTask> taskList = new ArrayList<>();
        try {
            taskRepository.findAll().forEach((JavaTask t) -> {
                taskList.add(t);
            });
        } catch (Exception e) {
            log.info("Cannot get tasks from the database!", e);
            throw new DAOSystemException("Cannot get tasks from the database!", e);
        }
        return taskList;
    }

//    @Override
//    public void createJavaTasksTable() throws DAOSystemException {
//        Connection conn = threadCache.get();
//        Statement stmt = null;
//        try {
//            stmt = conn.createStatement();
//            StringBuilder sb = new StringBuilder("CREATE TABLE java_tasks (")
//                    .append("task_id INT(11) AUTO_INCREMENT PRIMARY KEY,")
//                    .append("difficulty_group ENUM('BEGINNER', 'EXPERIENCED', 'EXPERT'),")
//                    .append("short_desc VARCHAR(255),")
//                    .append("elapsed_time INT(11),")
//                    .append("popularity INT DEFAULT 0,")
//                    .append("java_task_object LONGBLOB,")
//                    .append("reg_date DATE);");
//            stmt.execute(sb.toString());
//        } catch (SQLException e) {
//            log.info("Cannot create tasks table in database!", e);
//            throw new DAOSystemException("Cannot create tasks table in database!", e);
//        } finally {
//            close(stmt);
//        }
//    }
//
//    @Override
//    public void deleteJavaTasksTable() throws DAOSystemException {
//        Connection conn = threadCache.get();
//        Statement stmt = null;
//        try {
//            stmt = conn.createStatement();
//            stmt.execute("DROP TABLE java_tasks;");
//        } catch (SQLException e) {
//            log.info("Cannot delete tasks table in database!", e);
//            throw new DAOSystemException("Cannot delete tasks table in database!", e);
//        } finally {
//            close(stmt);
//        }
//    }

    /**
     * This method serializes java task into file before its saving to the database
     *
     * @param task task for serializing
     * @throws DAOSystemException in case of IO problems (file not found etc.) during task serializing
     */
    public static void serializeTask(JavaTask task) throws DAOSystemException {
        String filePath = "C:\\Users\\admin1\\IdeaProjects\\AdvancedJCW\\dao\\src\\main\\resources\\files\\Serial_task.txt";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(filePath)))) {
            out.writeObject(task);
        } catch (FileNotFoundException e) {
            throw new DAOSystemException("Can not find file to write JavaTask!", e);
        } catch (IOException e1) {
            throw new DAOSystemException("IO Exception while serializing JavaTask!", e1);
        }
    }
}
