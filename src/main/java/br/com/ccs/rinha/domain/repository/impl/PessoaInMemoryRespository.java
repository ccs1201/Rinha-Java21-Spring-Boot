package br.com.ccs.rinha.domain.repository.impl;

import br.com.ccs.rinha.domain.entity.Pessoa;
import br.com.ccs.rinha.domain.repository.InMemoryRespository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Repository
public class PessoaInMemoryRespository implements InMemoryRespository {

    private ConcurrentHashMap<UUID, Pessoa> map;
    private CopyOnWriteArrayList<Pessoa> copyOnWrite;

    public PessoaInMemoryRespository() {
        this.copyOnWrite = new CopyOnWriteArrayList<>(new ArrayList<>(50000));
        this.map = new ConcurrentHashMap<>(50000);
    }

    @Override
    public Optional<Pessoa> findByIdEager(UUID id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Pessoa> findByTermo(String t, PageRequest pageRequest) {

        return copyOnWrite
                .parallelStream()
                .filter(pessoa -> findTermo(t, pessoa))
                .limit(50).collect(Collectors.toList());
    }

    private boolean findTermo(String t, Pessoa pessoa) {
        return pessoa.getApelido().equals(t) || pessoa.getNome().equals(t) || pessoa.getStack().contains(t);
    }

    @Override
    public Pessoa save(Pessoa pessoa) {
        CompletableFuture.runAsync(() -> map.put(pessoa.getId(), pessoa), Executors.newVirtualThreadPerTaskExecutor());
        CompletableFuture.runAsync(() -> copyOnWrite.add(pessoa), Executors.newVirtualThreadPerTaskExecutor());
        return pessoa;
    }

    @Override
    public long count() {
        var count = copyOnWrite.size();
        copyOnWrite = new CopyOnWriteArrayList<>(new ArrayList<>(35000));
        map = new ConcurrentHashMap<>(35000);
        return count;
    }
}
