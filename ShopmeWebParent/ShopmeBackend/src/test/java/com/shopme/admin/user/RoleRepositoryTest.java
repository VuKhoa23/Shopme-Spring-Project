package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
// replace.NONE: do the test againts the real database, prevent JPA run test againts the in-memory db
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// tell JPA not to rollback after the test
@Rollback(false)
public class RoleRepositoryTest {
    @Autowired
    RoleRepository repo;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin", "Manage everything");
        // return a role in persistent context
        Role savedRole = repo.save(roleAdmin);
        // it will assert and rollback the transaction. So nothing will be saved in the databse. This is for testing only
        // It just create the table

        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void createOtherRoles(){
        Role roleSalePerson = new Role("Salesperson", "Manage product price, "
                                                            + "customers, shipping, orders and sales report");
        Role roleEditor = new Role("Editor", "Manage categories, brands "
                + "products, articles and menu");

        Role roleShipper = new Role("Shipper", "View products, view orders "
                + "and update order status");

        Role roleAssistant = new Role("Assistant", "Manage questions and reviews ");

        // return immutable list of saved roles
        repo.saveAll(List.of(roleSalePerson, roleEditor, roleShipper, roleAssistant));
    }
}


