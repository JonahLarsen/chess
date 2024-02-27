package service;

import model.GameData;

import java.util.Collection;

public record GamesListWrapper(Collection<GameData> games) {}
