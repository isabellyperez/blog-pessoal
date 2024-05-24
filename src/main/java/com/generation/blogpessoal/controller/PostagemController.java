package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController //anotação que diz para spring que essa é uma controladora de rotas e acesso aos metodos
@RequestMapping("/postagens") //rota para chegar nessa classe "insomnia"
@CrossOrigin(origins = "*", allowedHeaders ="*") //liberar o acesso a outras maquinas /allowedHeaders = liberar passagem de parametros no header

public class PostagemController {
	@Autowired //injeção de dependencias - instanciar a classe PostagemRepository
	private PostagemRepository postagemRepository;
	
	@Autowired 
	private TemaRepository temaRepository;
	
	@GetMapping //defini e verbo http que atende esse metodo
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll());
		//SELECT * FROM tb_postagens
		
	}
	
	//localhost:8080/postagens/1
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id){
		// findById = SELECT * FROM tb_postagens WHERE id = 1;
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	//SELECT * FROM tb_postagens WHERE titulo = "titulo";
	@GetMapping("/titulo/{titulo}")//localhost:8080/postagens/titulo/Postagem 02
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	//INSERT INTO tb_postagens (titulo, texto, data) VALUES ("Título", "Texto", "2024-12-31 14:05:01");
	@PostMapping //anotação PostMapping para ficar com o verbo correspondente com o que vamos executar - um cadastro novo
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
		if (temaRepository.existsById(postagem.getTema().getId()))
		//retorno em formato ResponseEntity
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(postagemRepository.save(postagem));	
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);

	}
	
	//UPDATE tb_postagem SET titulo = "...", texto = "..." Where id = 1
	@PutMapping //verbo http Put para o consumo no insomnia
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		if (postagemRepository.existsById(postagem.getId())) {
			if (temaRepository.existsById(postagem.getTema().getId())) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(postagemRepository.save(postagem));
			}
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Tema não existe!", null);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	//DELETE FROM tb_postagens WHERE id = id;
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Postagem> postagem = postagemRepository.findById(id);
	
		if(postagem.isEmpty()) //caso o objeto esteja vazio
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		//ele executa o response status de excessão, ele vai apenas colocar o botão como not found
		
		postagemRepository.deleteById(id);
		//caso contrário, objeto existir ele executa o metodo deleteById passando o id que informamos na url
	}
	
}
