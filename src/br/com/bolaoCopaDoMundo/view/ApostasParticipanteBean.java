package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.ApostaClassificacaoGrupo;
import br.com.bolaoCopaDoMundo.domain.Apostas;
import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.ApostaClassificacaoGrupoService;
import br.com.bolaoCopaDoMundo.service.ApostasService;
import br.com.bolaoCopaDoMundo.service.JogosService;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.util.FacesUtil;
import br.com.bolaoCopaDoMundo.util.RelatorioUtil;

@Component("apostasParticipanteBean")
@Scope("view")
public class ApostasParticipanteBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ApostasParticipanteBean.class);

	@Autowired
	private JogosService jogosService;

	@Autowired
	private ApostasService apostasService;

	@Autowired
	private LoginBean loginBean;

	@Autowired
	private ParticipanteService participanteService;

	@Autowired
	private ApostaClassificacaoGrupoService apostaClassificacaoGrupoService;

	@Autowired
	private RelatorioUtil relatorioUtil;

	// entidades das telas
	private Participante participante = new Participante();
	private Apostas aposta;
	private Long apostasGolNull;
	private String nomeParticipante = new String();

	// listas
	private List<Participante> participanteList = new ArrayList<Participante>();
	private List<Apostas> apostas;
	private List<ApostaClassificacaoGrupo> apostaGrupo;

	public void searchByParticipante() {
		try {
			apostas = apostasService.findByIdParticipante(participante.getId());
			apostaGrupo = apostaClassificacaoGrupoService
					.findByIdParticipante(participante.getId());
			System.out.println(apostaGrupo.size());
		} catch (Exception e) {
			apostas = new ArrayList<Apostas>();
			apostaGrupo = new ArrayList<ApostaClassificacaoGrupo>();
			FacesUtil.addErroMessage(e.getMessage());
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		if (participante.getNome() == null)
			FacesUtil.addErroMessage("Usuário não selecionado");
	}

	public void consultarParticipante() {
		try {
			participanteList = participanteService
					.searchByNome(getNomeParticipante());
		} catch (Exception e) {
			participanteList = new ArrayList<Participante>();
			FacesUtil.addErroMessage(e.getMessage());
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public void consultarParticipanteDialog() {
		consultarParticipante();

		if (!participanteList.isEmpty()) {
			RequestContext.getCurrentInstance().execute("dlg.show()");
		}

	}

	public void aposConsultarParticipante(Participante usu) {
		Participante p = new Participante();
		p.setId(getParticipante().getId());
		p = (Participante) usu;
		setParticipante(participanteService.findByParticipante(p));
		nomeParticipante = participante.getNome();
		
		//testar consulta automatica
		searchByParticipante();
	}

	public String limpaTela() {

		participante = new Participante();
		apostas = new ArrayList<Apostas>();
		apostaGrupo = new ArrayList<ApostaClassificacaoGrupo>();
		nomeParticipante = "";
		return "apostasParticipante";
	}

	/**
	 * Emitir Relatorio
	 * 
	 * @return
	 */
	public String relatorio() {

		try {
			FacesContext facesContext = FacesContext.getCurrentInstance(); 
			ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
			Map<String, Object> parametros = new HashMap<String, Object>();
			
			String pathRel = servletContext.getRealPath("//WEB-INF/relatorios/");  
			String filtro = " WHERE participante.idparticipante <> 1 ";
			
			parametros.put("FILTRO", filtro.toString());
			parametros.put("SUBREPORT_DIR",pathRel);
			relatorioUtil.relatorio("apostasParticipante.jasper", parametros,
					"apostasParticipante.pdf");
		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Erro na geração do Aposta por Participantes. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public List<Apostas> getApostas() {
		return apostas;
	}

	public void setApostas(List<Apostas> apostas) {
		this.apostas = apostas;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public Apostas getAposta() {
		return aposta;
	}

	public void setAposta(Apostas aposta) {
		this.aposta = aposta;
	}

	public Long getApostasGolNull() {
		return apostasGolNull;
	}

	public String getNomeParticipante() {
		return nomeParticipante;
	}

	public void setNomeParticipante(String nomeParticipante) {
		this.nomeParticipante = nomeParticipante;
	}

	public List<Participante> getParticipanteList() {
		return participanteList;
	}

	public void setParticipanteList(List<Participante> participanteList) {
		this.participanteList = participanteList;
	}

	public void setApostasGolNull(Long apostasGolNull) {
		this.apostasGolNull = apostasGolNull;
	}

	public List<ApostaClassificacaoGrupo> getApostaGrupo() {
		return apostaGrupo;
	}

	public void setApostaGrupo(List<ApostaClassificacaoGrupo> apostaGrupo) {
		this.apostaGrupo = apostaGrupo;
	}

}
