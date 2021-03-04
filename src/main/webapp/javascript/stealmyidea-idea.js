$(document).ready(
	function(){
		loadIdea();
});

console.log('blah blah blah.');

function loadIdea(){
	//get the idea number from the url
	var currentUrl = window.location.href;
	console.log('current url = ' + currentUrl);
	
	var ideaNumber = currentUrl.match('idea/(\\d+)')[1];
	//http://localhost:8080/stealmyidea/idea/12
	var url = '/stealmyidea/ideas?target=ideas&ideaNumber=' + ideaNumber;
	
	$.ajax({url: url,
		contentType: 'application/json; charset=UTF-8'}
	)
	.done(function(data) {
		
		var response = $.parseJSON(data);
		
		var successfulResquest = wasRequestSuccessful(response);
		
		if (successfulResquest){
			
			var ideas = response.data;
			
			if (isEmpty(ideas)){
				$('#content-container').empty();
				$('#content-container').append('<p>I have an idea ... Try looking for a valid idea.</p>');
				return;
			}
			
			var idea = ideas[0];
			
			var ideaHtml = createSingleIdeaHtml(idea);
			
			$('#content-container').append(ideaHtml);
		}
		else {
			if (doesResponseHaveMessages(response)){
				$('#messages-container').empty();
				console.log('messages...');
				console.log(response.messages);
				var messagesHtml = createMessagesHtml(response.messages);
				$('#messages-container').append(messagesHtml);
			}
		}
	})
	.fail(function() {
	})
	.always(function() {
	});
}

function createSingleIdeaHtml(idea){
	
	var ideaHtml = '<div id="idea-container-' + idea.ideaId + '" class="idea-container-single">' +
						'<div id="idea-number-container-' + idea.ideaId + '" class="idea-common-container idea-number-container">' + 
							'<span class="idea-common-label idea-idea-number-label">Idea number:</span>' +
							'<span class="idea-common-content idea-idea-number-content">' + nullToEmpty(idea.ideaNumber) + '</span>' + 
						'</div>' +
						'<div id="idea-idea-container-' + idea.ideaId + '" class="idea-common-container idea-idea-container">' + 
							'<span class="idea-common-label idea-idea-label">Idea:</span>' +
							'<span class="idea-common-content idea-idea-content">' + nullToEmpty(idea.idea) + '</span>' + 
						'</div>' +
						'<div id="idea-date-container-' + idea.ideaId + '" class="idea-common-container idea-date-container">' + 
							'<span class="idea-common-label idea-date-label">Date:</span>' +
							'<span class="idea-common-content idea-date-content">' + nullToEmpty(idea.ideaDate) + '</span>' + 
						'</div>' +
						'<div id="idea-steal-status-description-container-' + idea.ideaId + '" class="idea-common-container idea-steal-status-description-container">' + 
							'<span class="idea-common-label idea-steal-status-description-label">Has it been stolen?</span>' +
							'<span class="idea-common-content idea-steal-status-description-content">' + nullToEmpty(idea.stealStatusDescription) + '</span>' + 
						'</div>' +
						'<div id="idea-greatness-container-' + idea.ideaId + '" class="idea-common-container idea-greatness-container">' + 
							'<span class="idea-common-label idea-greatness-label">Greatness:</span>' +
							'<span class="idea-common-content idea-greatness-content">' + nullToEmpty(idea.greatness) + '</span>' + 
						'</div>' +
						'<div id="idea-description-container-' + idea.ideaId + '" class="idea-common-container idea-description-container">' +
							nullToEmpty(idea.description) +
						'</div>' + 
					'</div>';
	
	return ideaHtml;
}