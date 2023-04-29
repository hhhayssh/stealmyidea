/**
 * When the document's been loaded on the browser, we want to:
 * 
 * 		1. Go to the server and get the selection criteria (teams, players, initial values).
 * 		2. Initialize the UI based on those values. 
 */
$(document).ready(
	function(){
		$(window).scroll(function() {
			checkScrollAndLoadIdeas();
		});
		
		loadIdeasAndInitialize();
});

var GLOBAL_IDEA_CONTAINER = {
		totalIdeaCount: null,
		offset: 0,
		size: 4,
		sortFieldName: 'ideaNumber',
		sortDirection: 'descending',
		loadInProgress: false
};

function loadIdeasAndInitialize(){
	
	var url = 'ideas?target=ideaCount';
	
	$.ajax({url: url,
		contentType: 'application/json; charset=UTF-8'}
	)
	.done(function(rawResponse) {
		
		var response = $.parseJSON(rawResponse);
		
		var didItWork = wasRequestSuccessful(response);

		if (!didItWork){
			var areThereMessages = doesResponseHaveMessages(response);
			var messagesHtml = '<div>Unknown error</div>';
			if (areThereMessages){
				messagesHtml = createMessagesHtml(response.messages); 
			}
			setMainContent('<div>Error:' + messagesHtml + '</div>');
			return;
		}
		
		var totalIdeaCount = parseInt(response.data.ideaCount);
		
		updateTotalIdeaCount(totalIdeaCount);
		
		loadUntilScroll();
	});
}

var loadUntilScrollIntervalId = null;

function loadUntilScroll(){
	loadUntilScrollIntervalId = setInterval(loadUntilScrollInterval, 500);
}

function loadUntilScrollInterval(){

	var isScrollbarThere = isScrollbarVisible();

	if (!isScrollbarThere){
		checkAndLoadIdeas();
	}
	else {
		clearInterval(loadUntilScrollIntervalId);
	}
}

function isScrollbarVisible(){
	var mainContentHeight = $('#main-content-container').height();
	var viewportHeight = window.innerHeight;
	if (mainContentHeight > viewportHeight) {
	    return true;
	}
	return false;
}

function isScrollbarAtTheBottom(){
	//document.documentElement.clientHeight
//	if ((window.innerHeight + window.scrollY) >= document.body.scrollHeight) {
//    	return true;
//    }
//	if($('#main-content-container').scrollTop() == $('main-content-container').innerHeight()){
//	    //Reached the bottom
//		return true;
//	}
//	return false;
	
	if($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
		return true;
	}
	
	return false;
}

function checkScrollAndLoadIdeas(){
	var scrollbarAtTheBottom = isScrollbarAtTheBottom();
	if (scrollbarAtTheBottom) {
    	checkAndLoadIdeas();
    }
}

function isLoadInProgress(){
	return GLOBAL_IDEA_CONTAINER.loadInProgress;
}

function updateTotalIdeaCount(totalIdeaCount){
	GLOBAL_IDEA_CONTAINER.totalIdeaCount = totalIdeaCount;
}

function getLoadedIdeaCount(){
	return GLOBAL_IDEA_CONTAINER.offset;
}

function getTotalIdeaCount(){
	return GLOBAL_IDEA_CONTAINER.totalIdeaCount;
}

function areAllIdeasLoaded(){
	
	var loadedIdeaCount = getLoadedIdeaCount();
	var totalIdeaCount = getTotalIdeaCount();
	
	if (loadedIdeaCount >= totalIdeaCount){
		return true;
	}
	
	return false;
}

function shouldLoadIdeas(){
	
	if (isLoadInProgress()){
		return false;
	}
	
	var haveTheyAllBeenLoaded = areAllIdeasLoaded();
	
	if (haveTheyAllBeenLoaded){
		return false;
	}
	
	return true;
}

function setLoadInProgress(loadInProgress){
	GLOBAL_IDEA_CONTAINER.loadInProgress = loadInProgress;
}

function updateCurrentOffset(){
	
	var currentOffset = GLOBAL_IDEA_CONTAINER.offset;
	var size = GLOBAL_IDEA_CONTAINER.size;
	
	var newOffset = currentOffset + size;
	GLOBAL_IDEA_CONTAINER.offset = newOffset;
}

function setMainContent(html){
	$('#main-content-container').empty();
	$('#main-content-container').append(html);
}

function checkAndLoadIdeas(){
	
	var shouldIdeasBeLoaded = shouldLoadIdeas();
	
	if (shouldIdeasBeLoaded){
		loadIdeas();
	}
}

function loadIdeas(){
	
	setLoadInProgress(true);
	
	showLoadingContainer();
	
	var offset = GLOBAL_IDEA_CONTAINER.offset;
	var size = GLOBAL_IDEA_CONTAINER.size;
	var sortFieldName = GLOBAL_IDEA_CONTAINER.sortFieldName;
	var sortDirection = GLOBAL_IDEA_CONTAINER.sortDirection;
	
	var containerId = 'load-' + offset; 
	
	var url = 'ideas?target=ideas&offset=' + offset + '&size=' + size + '&sortFieldName=' + sortFieldName + '&sortDirection=' + sortDirection;
	
	var ideaLoadContainer = '<div id="' + containerId + '"></div>';
	
	$('#main-content-container').append(ideaLoadContainer);
	
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
				return;
			}
			
			updateCurrentOffset();
			
			var ideasHtml = createIdeasHtml(ideas);
			$('#' + containerId).empty();
			$('#' + containerId).append(ideasHtml);
			
			if (areAllIdeasLoaded()){
				$('#messages-container').empty();
				$('#messages-container').append('<div style="width: 100%; text-align: center; margin-top: 20px;">No more ideas to be found! Move along, sonny!</div>');
				return;
			}
		}
		else {
			if (doesResponseHaveMessages(response)){
				$('#messages-container').empty();
				var messagesHtml = createMessagesHtml(response.messages);
				$('#messages-container').append(messagesHtml);
			}
		}
		
		setLoadInProgress(false);
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