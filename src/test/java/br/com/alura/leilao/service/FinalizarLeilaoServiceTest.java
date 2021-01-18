package br.com.alura.leilao.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

@ExtendWith(SpringExtension.class)
class FinalizarLeilaoServiceTest {
	
	@InjectMocks
	private FinalizarLeilaoService service;
	
	@Mock
	private LeilaoDao leilaoDaoMock;			
	
	@Test
	void deveriaFinalizarUmLeilao() {	
		List<Leilao> leiloes = leiloes();		
		BDDMockito.when(leilaoDaoMock.buscarLeiloesExpirados()).thenReturn(leiloes);
		
		Leilao leilao = leiloes.get(0);
		
		service.finalizarLeiloesExpirados();
		
		assertThat(leilao.isFechado());
		assertThat(leilao.getLanceVencedor().getValor()).isEqualTo(new BigDecimal("900"));
		BDDMockito.verify(leilaoDaoMock).salvar(leilao);
	}
	
	private List<Leilao> leiloes() {
		List<Leilao> lista = new ArrayList<>();
		
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		
		Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
		
		Lance segundo = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));
		
		leilao.propoe(primeiro);
		leilao.propoe(segundo);
		
		lista.add(leilao);
		return lista;		
	}
}
