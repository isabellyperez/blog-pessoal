package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.blogpessoal.model.Postagem;

//JPaRepository - classe JPA  - metodos que vão realizar query no banco
public interface PostagemRepository extends JpaRepository<Postagem, Long> {
	//SELECT * FROM tb_postagens 
	
	//SELECT * FROM tb_postagens WHERE titulo LIKE "%POST%"; = findAllByTituloContainingIgnoreCase
	public List <Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);

}


