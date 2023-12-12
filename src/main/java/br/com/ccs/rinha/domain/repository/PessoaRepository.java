package br.com.ccs.rinha.domain.repository;

import br.com.ccs.rinha.domain.entity.Pessoa;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {

    String FIND_BY_ID = "from Pessoa p join fetch p.stack where p.id= :id";
    String FIND_BY_TERMO = """
            from Pessoa p join fetch p.stack s
             where p.nome ilike concat('%',:t,'%')
              or p.apelido ilike concat('%', :t, '%')
             """;

    @Query(FIND_BY_ID)
    Optional<Pessoa> findByIdEager(UUID id);

    @Query(value = FIND_BY_TERMO)
    @Transactional(readOnly = true)
    List<Pessoa> findByTermo(String t, PageRequest pageRequest);
}
