package com.fiap.farmacheck.service;

import com.fiap.farmacheck.exception.ResourceNotFoundException;
import com.fiap.farmacheck.kafka.MedicamentoIndisponivelProducer;
import com.fiap.farmacheck.mapper.MedicamentoMapper;
import com.fiap.farmacheck.model.dto.disponibilidade.DisponibilidadeResponseDTO;
import com.fiap.farmacheck.model.dto.disponibilidade.MedicamentoIndisponivelEvent;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoRequestDTO;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoResponseDTO;
import com.fiap.farmacheck.model.entity.Estoque;
import com.fiap.farmacheck.model.entity.Medicamento;
import com.fiap.farmacheck.repository.EstoqueRepository;
import com.fiap.farmacheck.repository.MedicamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicamentoService {

    private static final Logger logger = LoggerFactory.getLogger(MedicamentoService.class);

    private final MedicamentoRepository medicamentoRepository;
    private final EstoqueRepository estoqueRepository;
    private final MedicamentoMapper medicamentoMapper;
    private final MedicamentoIndisponivelProducer producer;

    public MedicamentoService(MedicamentoRepository medicamentoRepository,
                              EstoqueRepository estoqueRepository,
                              MedicamentoMapper medicamentoMapper,
                              MedicamentoIndisponivelProducer producer) {
        this.medicamentoRepository = medicamentoRepository;
        this.estoqueRepository = estoqueRepository;
        this.medicamentoMapper = medicamentoMapper;
        this.producer = producer;
    }

    public MedicamentoResponseDTO criar(MedicamentoRequestDTO dto) {
        Medicamento entity = medicamentoMapper.toEntity(dto);
        Medicamento salvo = medicamentoRepository.save(entity);
        logger.info("Medicamento cadastrado: {}", salvo.getNome());
        return medicamentoMapper.toResponse(salvo);
    }

    public List<MedicamentoResponseDTO> listarTodos() {
        return medicamentoRepository.findAll()
                .stream()
                .map(medicamentoMapper::toResponse)
                .toList();
    }

    public MedicamentoResponseDTO buscarPorId(Integer id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado com ID: " + id));
        return medicamentoMapper.toResponse(medicamento);
    }

    public MedicamentoResponseDTO atualizar(Integer id, MedicamentoRequestDTO dto) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado com ID: " + id));
        medicamentoMapper.updateEntityFromDto(dto, medicamento);
        Medicamento atualizado = medicamentoRepository.save(medicamento);
        logger.info("Medicamento atualizado: {}", atualizado.getNome());
        return medicamentoMapper.toResponse(atualizado);
    }

    public void deletar(Integer id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado com ID: " + id));
        medicamentoRepository.delete(medicamento);
        logger.info("Medicamento deletado: {}", medicamento.getNome());
    }

    public DisponibilidadeResponseDTO verificarDisponibilidade(String nomeMedicamento, String emailUsuario, String nomeUsuario) {
        List<Estoque> estoques = estoqueRepository.findByMedicamentoNomeIgnoreCaseAndQuantidadeGreaterThan(
                nomeMedicamento, 0);

        if (!estoques.isEmpty()) {
            logger.info("Medicamento '{}' esta disponivel no estoque", nomeMedicamento);
            return new DisponibilidadeResponseDTO(
                    nomeMedicamento,
                    true,
                    "Medicamento disponível na farmácia pública"
            );
        }

        logger.info("Medicamento '{}' NAO disponivel. Enviando para fila Kafka. Usuario: {}", nomeMedicamento, emailUsuario);

        MedicamentoIndisponivelEvent event = new MedicamentoIndisponivelEvent(
                nomeMedicamento,
                emailUsuario,
                nomeUsuario,
                LocalDateTime.now()
        );
        producer.enviar(event);

        return new DisponibilidadeResponseDTO(
                nomeMedicamento,
                false,
                "Medicamento não disponível. Sua pesquisa foi registrada e você será notificado quando estiver disponível."
        );
    }
}
