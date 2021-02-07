/**
 * When the document's been loaded on the browser, we want to:
 * 
 * 		1. Go to the server and get the selection criteria (teams, players, initial values).
 * 		2. Initialize the UI based on those values. 
 */
$(document).ready(
	function(){
		loadIdeasAndInitialize();
});

function loadIdeasAndInitialize(){
	loadIdeas();
}

function wasRequestSuccessful(response){
	
	if (!isDefined(response)){
		return false;
	}
	
	var status = response.status;
	if (!isDefined(status)){
		return false;
	}
	
	if ('SUCCESS' == status){
		return true;
	}
	
	return false;
}

function doesResponseHaveMessages(response){
	
	if (!isDefined(response)){
		return false;
	}
	
	var messages = repsonse.messages;
	if (!isEmpty(messages)){
		return true;
	}
	
	return false;
}

function loadIdeas(){
	
	showLoadingContainer();
	
	$.ajax({url: 'stealmyidea?target=ideas',
		contentType: 'application/json; charset=UTF-8'}
	)
	.done(function(data) {
		hideLoadingContainer();
		
		var response = $.parseJSON(data);
		
		var successfulResquest = wasRequestSuccessful(response);
		
		if (successfulResquest){
			
			var ideas = response.data;
			
			if (ideas.length == 0){
				$('#main-content-container').append('<div>No ideas to be found! Move along, sonny!</div>');
				return;
			}
			
			var ideasHtml = createIdeasHtml(ideas);
			$('#main-content-container').append(ideasHtml);
		}
		else {
			if (doesResponseHaveMessages(response)){
				$('#messages-container').empty();
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

function showLoadingContainer(){
	$('#loading-container').show();
}

function hideLoadingContainer(){
	$('#loading-container').hide();
}