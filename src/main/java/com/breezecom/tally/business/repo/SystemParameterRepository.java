package com.breezecom.tally.business.repo;

import org.springframework.data.repository.CrudRepository;

import com.breezecom.tally.model.SystemParameter;

public interface SystemParameterRepository extends CrudRepository<SystemParameter, Long> {
    SystemParameter findByName(String name);
}