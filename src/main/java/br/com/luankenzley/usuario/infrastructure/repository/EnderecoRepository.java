package br.com.luankenzley.usuario.infrastructure.repository;

import br.com.luankenzley.usuario.infrastructure.entity.EnderecoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnderecoRepository extends MongoRepository<EnderecoEntity, String> {
    EnderecoEntity findByUsuarioId(String usuarioId);

    void deleteByUsuarioId(String usuarioId);
}
