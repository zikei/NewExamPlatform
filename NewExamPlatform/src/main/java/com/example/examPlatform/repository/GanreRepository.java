package com.example.examPlatform.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.examPlatform.entity.Ganre;

/**
 * Ganreテーブル：リポジトリ
 */
public interface GanreRepository extends CrudRepository<Ganre, Integer> {

}