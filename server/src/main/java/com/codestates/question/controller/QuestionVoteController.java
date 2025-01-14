package com.codestates.question.controller;

import java.security.Principal;

import javax.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codestates.question.entity.QuestionVote;
import com.codestates.question.service.QuestionService;
import com.codestates.question.service.QuestionVoteService;
import com.codestates.status.VoteStatus;
import com.codestates.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/questions/{question_id}/votes")
@RequiredArgsConstructor
public class QuestionVoteController {
	private final QuestionVoteService questionVoteService;
	private final QuestionService questionService;
	private final UserService userService;

	@PostMapping("/up")
	public ResponseEntity postVoteUp(@PathVariable("question_id") @Positive Long questionId,
		Principal principal) {
		QuestionVote questionVote = buildVote(
			questionId,
			principal.getName(),
			VoteStatus.UP
		);

		questionVoteService.updateStatusOrCreateVoteIfNotExist(questionVote);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/up")
	public ResponseEntity deleteVoteUp(@PathVariable("question_id") @Positive Long questionId,
		Principal principal) {
		QuestionVote questionVote = buildVote(
			questionId,
			principal.getName(),
			VoteStatus.UP
		);

		questionVoteService.deleteQuestionVote(questionVote);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/down")
	public ResponseEntity postVoteDown(@PathVariable("question_id") @Positive Long questionId,
		Principal principal) {
		QuestionVote questionVote = buildVote(
			questionId,
			principal.getName(),
			VoteStatus.DOWN
		);

		questionVoteService.updateStatusOrCreateVoteIfNotExist(questionVote);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/down")
	public ResponseEntity deleteVoteDown(@PathVariable("question_id") @Positive Long questionId,
		Principal principal) {
		QuestionVote questionVote = buildVote(
			questionId,
			principal.getName(),
			VoteStatus.DOWN
		);

		questionVoteService.deleteQuestionVote(questionVote);

		return ResponseEntity.ok().build();
	}

	private QuestionVote buildVote(Long questionId, String email, VoteStatus status) {
		QuestionVote questionVote = QuestionVote.builder()
			.status(status)
			.build();
		questionVote.setQuestion(questionService.findVerifiedQuestion(questionId));
		questionVote.setUser(userService.findUserByEmail(email));

		return questionVote;
	}
}
