package com.victorgolovanov.TinkoffService.service;

import com.victorgolovanov.TinkoffService.exception.StockNotfoundException;
import com.victorgolovanov.TinkoffService.model.Currency;
import com.victorgolovanov.TinkoffService.model.Stock;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.MarketContext;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrument;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {

    private final OpenApi openApi;

    @Override
    public Stock getStockByTicker(String ticker) {
        MarketContext marketContext = openApi.getMarketContext();
        CompletableFuture<MarketInstrumentList> listCF = marketContext.searchMarketInstrumentsByTicker(ticker);
        List<MarketInstrument> list = listCF.join().getInstruments();
        if (list.isEmpty()) {
            throw new StockNotfoundException(String.format("Stock %S not found.", ticker));
        }
        MarketInstrument item = list.get(0);

        return new Stock(item.getTicker(), item.getFigi(), item.getName(), item.getType().getValue(), Currency.valueOf(item.getCurrency().getValue()), "TINKOFF");
    }
}
