package br.com.bolaoCopaDoMundo.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;
import br.com.bolaoCopaDoMundo.service.ParticipanteService;
import br.com.bolaoCopaDoMundo.util.BolaoUtil;
import br.com.bolaoCopaDoMundo.util.FacesUtil;

@SuppressWarnings("serial")
@Component("participanteListBean")
@Scope("session")
public class ParticipanteListBean implements Serializable {

	static Logger logger = Logger.getLogger(ParticipanteListBean.class);

	@Autowired
	private ParticipanteService ParticipanteService;

	@Autowired
	private BolaoUtil bolaoUtil;

	private String nome = new String();
	private int ativo = -1;// traz a lista toda

	private Participante participante = new Participante();
	private List<Participante> participanteList = new ArrayList<Participante>();

	private LazyDataModel<Participante> model;

	// util
	// verifica se já foi feita alguma consulta
	private boolean consultar = false;

	public String ParticipanteByNome() {

		try {
			consultar = true;
			participanteList = ParticipanteService.searchByNome(nome);

		} catch (Exception e) {
			participanteList = new ArrayList<Participante>();
			FacesUtil.addErroMessage(e.getMessage());
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "participanteList?faces-redirect=true";
	}

	public String consultar() {

		consultar = true;
		model = new LazyDataModel<Participante>() {

			@Override
			public List<Participante> load(int inicio, int maxPerPage,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				participanteList = ParticipanteService.findByNome(nome, ativo,
						inicio, maxPerPage);
				setPageSize(maxPerPage);
				setRowCount(ParticipanteService.count(nome, ativo));
				return participanteList;
			}

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}
		};
		return "participanteList";
	}

	public String resetarSenha() {

		try {
			String password = new String("Tce12345");
			try {
				participante.setPassword(bolaoUtil.criptografarSenha(password
						.toUpperCase()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			ParticipanteService.salvar(participante);
			participante = new Participante();

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (BolaoCopaDoMundoRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil
					.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		return "participanteList";
	}

	public String limpaTela() {
		ativo = -1;
		participante = new Participante();
		nome = new String();
		participanteList = new ArrayList<Participante>();
		this.model = new LazyDataModel<Participante>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Participante> load(int inicio, int maxPerPage,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				return participanteList;
			}

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}
		};
		consultar = false;

		return "participanteList?faces-redirect=true";
	}

	/**
	 * Getters e Setters
	 */

	public Participante getParticipante() {
		return participante;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setParticipante(Participante Participante) {
		this.participante = Participante;
	}

	public List<Participante> getParticipanteList() {
		return participanteList;
	}

	public void setParticipanteList(List<Participante> participanteList) {
		this.participanteList = participanteList;
	}

	public boolean isConsultar() {
		return consultar;
	}

	public void setConsultar(boolean consultar) {
		this.consultar = consultar;
	}

	public LazyDataModel<Participante> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Participante> model) {
		this.model = model;
	}

	public int getAtivo() {
		return ativo;
	}

	public void setAtivo(int ativo) {
		this.ativo = ativo;
	}

}
