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

var GLOBAL_IDEA_CONTAINER = {
		offset: 0,
		size: 4,
		sortFieldName: 'ideaNumber',
		sortDirection: 'descending',
		loadMoreIdeas: true
};

window.onscroll = function(ev) {
    if ((window.innerHeight + window.scrollY) >= document.body.scrollHeight) {
      if (GLOBAL_IDEA_CONTAINER.loadMoreIdeas){
    	  loadIdeas();
      }
    }
};

function loadIdeasAndInitialize(){
	loadIdeas();
}



function updateCurrentOffset(){
	
	var currentOffset = GLOBAL_IDEA_CONTAINER.offset;
	var size = GLOBAL_IDEA_CONTAINER.size;
	
	var newOffset = currentOffset + size;
	GLOBAL_IDEA_CONTAINER.offset = newOffset;
}

function loadIdeas(){
	
	showLoadingContainer();
	GLOBAL_IDEA_CONTAINER.loadMoreIdeas = false;
	
	var offset = GLOBAL_IDEA_CONTAINER.offset;
	var size = GLOBAL_IDEA_CONTAINER.size;
	var sortFieldName = GLOBAL_IDEA_CONTAINER.sortFieldName;
	var sortDirection = GLOBAL_IDEA_CONTAINER.sortDirection;
	
	var url = 'ideas?target=ideas&offset=' + offset + '&size=' + size + '&sortFieldName=' + sortFieldName + '&sortDirection=' + sortDirection;
	
	$.ajax({url: url,
		contentType: 'application/json; charset=UTF-8'}
	)
	.done(function(data) {
		hideLoadingContainer();
		
		var response = $.parseJSON(data);
		
		var successfulResquest = wasRequestSuccessful(response);
		
		if (successfulResquest){
			
			var ideas = response.data;
			
			if (ideas.length == 0){
				$('#messages-container').empty();
				$('#messages-container').append('<div style="width: 100%; text-align: center; margin-top: 20px;">No more ideas to be found! Move along, sonny!</div>');
				GLOBAL_IDEA_CONTAINER.loadMoreIdeas = false;
				return;
			}
			
			updateCurrentOffset();
			
			var ideasHtml = createIdeasHtml(ideas);
			// need to append each idea to its own container so the order stays the same so scrolling too fast
			//doesn't mess it up
			$('#main-content-container').append(ideasHtml);
		}
		else {
			if (doesResponseHaveMessages(response)){
				$('#messages-container').empty();
				var messagesHtml = createMessagesHtml(response.messages);
				$('#messages-container').append(messagesHtml);
			}
		}
		
		GLOBAL_IDEA_CONTAINER.loadMoreIdeas = true;
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