package com.victorgolovanov.TinkoffService.service;

import com.victorgolovanov.TinkoffService.exception.StockNotfoundException;
import com.victorgolovanov.TinkoffService.model.Currency;
import com.victorgolovanov.TinkoffService.model.Stock;
import dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrument;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TinkoffStockService implements StockService {

    private final OpenApi openApi;

    @Async
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentTicker(String ticker) {
        return openApi.getMarketContext().searchMarketInstrumentsByTicker(ticker);
    }

    @Override
    public Stock getStockByTicker(String ticker) {
        CompletableFuture<MarketInstrumentList> cf = getMarketInstrumentTicker(ticker);
        List<MarketInstrument> list = cf.join().getInstruments();
        if (list.isEmpty()) {
            throw new StockNotfoundException(String.format("Stock %S not found.", ticker));
        }
        MarketInstrument item = list.get(0);

        return new Stock(item.getTicker(), item.getFigi(), item.getName(), item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()), "TINKOFF");
    }

    @Override
    public StocksDto getStocksByTickers(TickersDto tickers) {
        List<CompletableFuture<MarketInstrumentList>> marketInstruments = new ArrayList<>();
        tickers.getTickers().forEach(ticker -> marketInstruments.add(getMarketInstrumentTicker(ticker)));
        List<Stock> stocks = marketInstruments.stream()
                // wait while all requests are done
                .map(CompletableFuture::join)
                .map(mi -> {
                    if (!mi.getInstruments().isEmpty()) {
                        return mi.getInstruments().get(0);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(mi -> new Stock(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        mi.getType().getValue(),
                        Currency.valueOf(mi.getCurrency().getValue()),
                        "TINKOFF"
                )).collect(Collectors.toList());

        return new StocksDto(stocks);
    }

    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi) {
        CompletableFuture<Optional<Orderbook>> orderBook = openApi.getMarketContext().getMarketOrderbook(figi, 0);
        log.info("Getting price {} from Tinkoff", figi);
        return orderBook;
    }

    @Override
    public StocksPricesDto getStocksPrices(FigiesDto figies) {
        long start = System.currentTimeMillis();
        List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
        figies.getFigies().forEach(figi -> orderBooks.add(getOrderBookByFigi(figi)));
        List<StockPrice> prices = orderBooks.stream()
                // wait while all requests are done
                .map(CompletableFuture::join)
                .map(ob -> ob.orElseThrow(() -> new StockNotfoundException("Stock not found ")))
                .map(ob -> new StockPrice(
                        ob.getFigi(),
                        ob.getLastPrice()
                )).collect(Collectors.toList());
        log.info("Time - {} ", System.currentTimeMillis() - start);
        return new StocksPricesDto(prices);
    }
}
