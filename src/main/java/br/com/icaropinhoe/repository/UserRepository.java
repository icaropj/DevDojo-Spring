package br.com.icaropinhoe.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.icaropinhoe.model.ApiUser;

public interface UserRepository extends PagingAndSortingRepository<ApiUser, Long>{
	
	ApiUser findByUsername(String username);
	
}
