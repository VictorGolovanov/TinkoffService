package com.victorgolovanov.TinkoffService.controller;

import com.victorgolovanov.TinkoffService.model.Stock;
import com.victorgolovanov.TinkoffService.service.StockService;
import dto.FigiesDto;
import dto.StocksDto;
import dto.StocksPricesDto;
import dto.TickersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker);
    }

    @PostMapping("/stocks/stocksbytickers")
    public StocksDto getStocks(@RequestBody TickersDto tickers) {
        return stockService.getStocksByTickers(tickers);
    }

    @PostMapping("/prices")
    public StocksPricesDto getPrices(@RequestBody FigiesDto figies) {
        return stockService.getStocksPrices(figies);
    }
}
