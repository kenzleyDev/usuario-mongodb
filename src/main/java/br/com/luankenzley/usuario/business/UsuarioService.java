package br.com.luankenzley.usuario.business;

import br.com.luankenzley.usuario.api.converter.UsuarioConverter;
import br.com.luankenzley.usuario.api.converter.UsuarioMapper;
import br.com.luankenzley.usuario.api.request.UsuarioRequestDTO;
import br.com.luankenzley.usuario.api.response.UsuarioResponseDTO;
import br.com.luankenzley.usuario.infrastructure.entity.EnderecoEntity;
import br.com.luankenzley.usuario.infrastructure.entity.UsuarioEntity;
import br.com.luankenzley.usuario.infrastructure.exceptions.BusinessException;
import br.com.luankenzley.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final UsuarioMapper usuarioMapper;
    private final EnderecoService enderecoService;


    public UsuarioEntity salvaUsuario(UsuarioEntity usuarioEntity) {
        return usuarioRepository.save(usuarioEntity);
    }

    public UsuarioResponseDTO gravarUsuarios(UsuarioRequestDTO usuarioRequestDTO) {
        try {
            notNull(usuarioRequestDTO, "Os dados do usuário são obrigatórios");
            UsuarioEntity usuarioEntity = salvaUsuario(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO));
            EnderecoEntity enderecoEntity = enderecoService.salvaEndereco(
                    usuarioConverter.paraEnderecoEntity(usuarioRequestDTO.getEndereco(), usuarioEntity.getId()));
            return usuarioMapper.paraUsuarioResponseDTO(usuarioEntity, enderecoEntity);
        } catch (Exception e) {
            throw new BusinessException("Erro ao gravar dados de usuário", e);
        }
    }


    public UsuarioResponseDTO buscaDadosUsuario(String email) {
        try {
            UsuarioEntity entity = usuarioRepository.findByEmail(email);
            notNull(entity, "Usuário não encontrado");
            EnderecoEntity enderecoEntity = enderecoService.findByUsuarioId(entity.getId());

            return usuarioMapper.paraUsuarioResponseDTO(entity, enderecoEntity);
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar dados de usuário", e);
        }
    }

    @Transactional
    public void deletaDadosUsuario(String email) {
        UsuarioEntity entity = usuarioRepository.findByEmail(email);
        usuarioRepository.deleteByEmail(email);
        enderecoService.deleteByUsuarioId(entity.getId());
    }
}
