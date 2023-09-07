package com.vinsguru.webfluxfileupload.controller;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeId;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;

@RestController
@RequestMapping("upload")
public class UploadController {

	private final Path basePath = Paths.get("./src/main/resources/upload/");

	@PostMapping("file/single")
	public Mono<Void> upload(@RequestPart("user-name") String name,
			@RequestPart("fileToUpload") Mono<FilePart> filePartMono) {
		System.out.println("user : " + name);
		return filePartMono.doOnNext(fp -> System.out.println("Received File : " + fp.filename()))
				.flatMap(fp -> fp.transferTo(basePath.resolve(fp.filename()))).then();
	}

	@PostMapping("file/multi1")
	public Mono<Void> upload(@RequestPart("files") Flux<FilePart> partFlux) {
		return partFlux.doOnNext(fp -> System.out.println(fp.filename()))
				.flatMap(fp -> fp.transferTo(basePath.resolve(fp.filename()))).then();
	}

	@JsonProperty
	public static void main(String[] args) {
		// code {"d", "e", "co"} - true
		// catsandog {"cats", "cat", "dog", "sand", "and"} - false
		// cars {"car","ca","rs"} - true
		// purple {"p", "ur", "le", "purpl"} - true

		 System.out.println("code: "+matchingWord("code", new String[] {"d", "e", "co"}));;
		 System.out.println("catsandog: "+matchingWord("catsandog", new String[] {"cats", "cat", "dog", "sand", "and"}));;
		 System.out.println("cars: "+matchingWord("cars", new String[] {"car","ca","rs"} ));;
		 System.out.println("purple: "+matchingWord("purple", new String[] {"p", "ur", "le", "purpl"}));;
	}

	public static boolean matchingWord(String word, String[] codes) {
		String tmp = word;
		List<String> asList = Arrays.asList(codes);
		asList.sort((c1,c2)->c1.compareTo(c2));
		for (String c : asList) {
			if (tmp.contains(c)) {
				tmp=tmp.replace(c, "");
			}
		}
		return tmp.isEmpty();
	}
}