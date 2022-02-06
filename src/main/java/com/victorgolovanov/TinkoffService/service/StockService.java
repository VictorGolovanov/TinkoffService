package com.victorgolovanov.TinkoffService.service;

import com.victorgolovanov.TinkoffService.model.Stock;
import dto.FigiesDto;
import dto.StocksDto;
import dto.StocksPricesDto;
import dto.TickersDto;

public interface StockService {
    Stock getStockByTicker(String ticker);
    StocksDto getStocksByTickers(TickersDto tickers);
    StocksPricesDto getStocksPrices(FigiesDto figies);
}
