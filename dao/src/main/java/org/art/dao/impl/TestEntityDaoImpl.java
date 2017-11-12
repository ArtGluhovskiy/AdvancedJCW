package org.art.dao.impl;

import lombok.Data;
import org.art.dao.DAO;
import org.art.dao.exceptions.DAOSystemException;
import org.art.dao.repository.TestEntityRepository;
import org.art.entities.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Data
@Repository
public class TestEntityDaoImpl implements DAO<TestEntity> {

    private TestEntityRepository testEntityRepository;

    @Autowired
    public void setTestEntityRepository(TestEntityRepository testEntityRepository) {
        this.testEntityRepository = testEntityRepository;
    }

    @Override
    public TestEntity save(TestEntity testEntity) throws DAOSystemException {
        return testEntityRepository.save(testEntity);
    }

    @Override
    public TestEntity get(Long id) throws DAOSystemException {
        return testEntityRepository.findById(id).orElse(null);
    }

    @Override
    public TestEntity update(TestEntity testEntity) throws DAOSystemException {
        return testEntityRepository.save(testEntity);
    }

    @Override
    public void delete(Long id) throws DAOSystemException {
        testEntityRepository.deleteById(id);
    }
}
