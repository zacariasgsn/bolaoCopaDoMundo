package br.com.bolaoCopaDoMundo.dao;

import java.util.List;

import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.Jogos;
import br.com.bolaoCopaDoMundo.domain.Participante;

public interface ApostasDAO {

	public Apostas salvar(Apostas aposta);

	public List<Apostas> findByUsername(String username);
	
	public List<Apostas> findByIdParticipante(Long idParticipante);
	
	public List<Apostas> findByJogo(Long idJogo, int first, int rows);
	
	public Long countByParticipanteAndGolNull(Participante participante);
	
	public int countByJogo (Long idJogo);
	
	public Apostas getApostaByJogoParticipante(Participante participante, Jogos jogo);

}
