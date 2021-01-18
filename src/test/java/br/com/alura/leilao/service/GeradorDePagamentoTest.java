package br.com.alura.leilao.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

@ExtendWith(SpringExtension.class)
class GeradorDePagamentoTest {

	@InjectMocks
	private GeradorDePagamento geradorDePagamento;
	
	@Mock
	private PagamentoDao pagamentoDaoMock;
	
	@Mock
	private Clock clock;
	
	@Captor
	private ArgumentCaptor<Pagamento> captor;
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		LocalDate data = LocalDate.of(2020, 12, 7);	
		Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
		
		BDDMockito.when(clock.instant()).thenReturn(instant);
		
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLances().get(0);		
		geradorDePagamento.gerarPagamento(vencedor);			
		
		BDDMockito.verify(pagamentoDaoMock).salvar(captor.capture());
		
		Pagamento pagamento = captor.getValue();
		
		assertThat(LocalDate.now().plusDays(1)).isEqualTo(pagamento.getVencimento());		
		assertThat(pagamento.getValor()).isEqualTo(vencedor.getValor());
		assertThat(pagamento.getPago()).isFalse();
		assertThat(vencedor.getUsuario()).isEqualTo(pagamento.getUsuario());
		assertThat(leilao).isEqualTo(pagamento.getLeilao());
	}
	
	private Leilao leilao() {				
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
					
		Lance lance = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));
				
		leilao.propoe(lance);
		leilao.setLanceVencedor(lance);
				
		return leilao;		
	}
}
