package org.art.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.dao.JavaTaskDao;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.repository.JavaTaskRepository;
import org.art.entities.JavaTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Java Task Dao implementation.
 */
@Repository
public class JavaTaskDaoImpl implements JavaTaskDao {

    public static final Logger LOG = LogManager.getLogger(JavaTaskDaoImpl.class);

    //Path to the file where serialized java task is stored
    private static final String SERIAL_TASK_PATH = "C:\\Users\\admin1\\IdeaProjects\\AdvancedJCW\\dao\\src\\main\\resources\\etc\\serial-tasks\\task3.txt";

    private final JavaTaskRepository taskRepository;

    @Autowired
    public JavaTaskDaoImpl(JavaTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public JavaTask save(JavaTask javaTask) throws DAOSystemException {
        JavaTask savedTask;
        try {
            serializeTask(javaTask);
            Path path = Paths.get(SERIAL_TASK_PATH);
            byte[] data = Files.readAllBytes(path);
            javaTask.setBinTask(data);
            savedTask = taskRepository.save(javaTask);
        } catch (Exception e) {
            LOG.info("Cannot save task into database!", e);
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
            LOG.info("Can not get task from the database!", e);
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
            LOG.info("Cannot update task in the database!", e);
            throw new DAOSystemException("Cannot update task in the database!", e);
        }
        return updTask;
    }

    @Override
    public void delete(Long id) throws DAOSystemException {
        try {
            taskRepository.deleteById(id);
        } catch (Exception e) {
            LOG.info("Cannot delete task from the database!", e);
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
            LOG.info("Cannot get task from database!", e);
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
            LOG.info("Cannot get tasks from the database!", e);
            throw new DAOSystemException("Cannot get task from the database!", e);
        }
        return taskList;
    }

    @Transactional(value = Transactional.TxType.REQUIRED)
    @Override
    public void increaseTaskPopularity(JavaTask task) throws DAOSystemException {
        try {
            taskRepository.updateTaskPopularity(task.getPopularity() + 1, task.getTaskId());
        } catch (Exception e) {
            LOG.info("Cannot update task popularity in the database!", e);
            throw new DAOSystemException("Cannot update task popularity in the database!", e);
        }
    }

    @Override
    public List<JavaTask> getAll() throws DAOSystemException {
        List<JavaTask> taskList = new ArrayList<>();
        try {
            taskRepository.findAll().forEach(taskList::add);
        } catch (Exception e) {
            LOG.info("Cannot get tasks from the database!", e);
            throw new DAOSystemException("Cannot get tasks from the database!", e);
        }
        return taskList;
    }

    /**
     * This method serializes java task into file before its saving to the database
     *
     * @param task task for serializing
     * @throws DAOSystemException in case of IO problems (file not found etc.) during task serializing
     */
    private static void serializeTask(JavaTask task) throws DAOSystemException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(SERIAL_TASK_PATH)))) {
            out.writeObject(task);
        } catch (FileNotFoundException e) {
            throw new DAOSystemException("Can not find file to write JavaTask!", e);
        } catch (IOException e1) {
            throw new DAOSystemException("IO Exception while serializing JavaTask!", e1);
        }
    }
}
