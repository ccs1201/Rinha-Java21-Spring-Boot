package br.com.ccs.rinha.api.controller;

import br.com.ccs.rinha.api.model.PessaoInput;
import br.com.ccs.rinha.domain.entity.Pessoa;
import br.com.ccs.rinha.domain.repository.PessoaRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping
public class PessoaController {

    private static final String PATH = "/pessoas";
    private static final String URI_STR = "/pessoas/";
    @Autowired
    private PessoaRepository repository;
    //    private final InMemoryRespository repository;
    private static final PageRequest pageRequest = PageRequest.of(0, 50);

    @PostMapping(PATH)
    @ResponseStatus(CREATED)
    public ResponseEntity<Pessoa> create(@RequestBody @Valid PessaoInput input) {

        var p = input.toPessoa();

        try {
            CompletableFuture.runAsync(() -> repository.save(p), Executors.newVirtualThreadPerTaskExecutor());
            return ResponseEntity.created(URI.create(
                    URI_STR.concat(p.getId().toString()))).build();
        } catch (Exception e) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(PATH + "/{id}")
    @ResponseStatus(OK)
    public Pessoa findById(@PathVariable @NotNull UUID id) {
        return repository
                .findByIdEager(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping(PATH)
    @ResponseStatus(OK)
    public List<Pessoa> findByTermo(@Nullable String t) {
        if (Objects.isNull(t)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        return repository.findByTermo(t, pageRequest);
    }

    @GetMapping("contagem-pessoas")
    @ResponseStatus(OK)
    public Long contarPessoas() {
        return repository.count();
    }
}
