package dto;

import com.victorgolovanov.TinkoffService.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StocksDto {
    List<Stock> stocks;
}
