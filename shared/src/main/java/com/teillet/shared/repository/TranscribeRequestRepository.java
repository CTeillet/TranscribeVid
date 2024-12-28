package com.teillet.shared.repository;

import com.teillet.shared.model.LaunchProcessRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscribeRequestRepository extends CrudRepository<LaunchProcessRequest, String> {

}
