package com.omoke.store.repositories;

import com.omoke.store.users.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
