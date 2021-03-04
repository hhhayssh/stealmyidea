function addIdea(){
	disableAddButton();
	
	var isIdeaOk = checkIdea();
	
	if (!isIdeaOk){
		enableAddButton();
		return;
	}
	
	var idea = {
			idea: $('#idea').val(),
			ideaDate: $('#idea-date').val(),
			stealStatus: $('#steal-status').val(),
			stealStatusDescription: $('#steal-status-description').val(),
			greatness: $('#greatness').val(),
			description: $('#description').val()
	};
	
	var ideaJsonToSend = JSON.stringify(idea);
	
	$.ajax({url: 'add',
			method: 'POST',
			data: ideaJsonToSend,
			contentType: 'application/json; charset=UTF-8'}
	)
	.done(function(data) {
		//var selectionCriteriaContainer = $.parseJSON(data);
		var response = $.parseJSON(data);
		
		if ('SUCCESS' == response.status){
			hideAddButton();
			$('#message-container-add').append('<div>Your idea was successfully added ... Yoink!  You can see it on the <a href="/">front page</a> now.</div>');
		}
		else if ('ERROR' == response.status){
			showAddButton();
			enableAddButton();
			
			if (!isEmpty(response.messages)){
				var messageHtml = '';
				
				for (var index = 0; index < response.messages.length; index++){
					var message = response.messages[index];
					$('#message-container-add').append('<div>' + message + '</div>');
				}
			}
			
			if (!isBlank(response.statusCode)){
				if ('IDEA_ALREADY_EXISTS' == response.statusCode){
					$('#message-container-idea').append('<div>LOL it\'s already taken... Guess you weren\'t as clever as you thought.</div>');
				}
			}
		}
		
	})
	.fail(function() {
	})
	.always(function() {
	});
}

function checkIdea(){
	
	clearMessages();
	
	var error = false;

	var idea = $('#idea').val();
	if (isBlank(idea)){
		$('#message-container-idea').append('<span>Hey, man, can\'t add your idea if you don\'t put in something!</span>');
		error = true;
	}
	else {
		if (idea.length > 100){
			$('#message-container-idea').append('<span>Keep the name brief, buddy (use 100 characters or less).</span>');
			error = true;
		}
	}
	
	var ideaDate = $('#idea-date').val();
	if (ideaDate.length > 100){
		$('#message-container-idea-date').append('<span>Tl;dr ... Your idea date is too long (use 100 characters or less).</span>');
		error = true;
	}
	
	var stealStatus = $('#steal-status').val();
	if (stealStatus.length > 100){
		$('#message-container-steal-status').append('<span>Just the status, ma\'am (use 100 characters or less).</span>');
		error = true;
	}
	
	var stealStatusDescription = $('#steal-status-description').val();
	if (stealStatusDescription.length > 500){
		$('#message-container-steal-status-description').append('<span>If it\'s that involved, consult a lawyer (use 500 characters or less).</span>');
		error = true;
	}
	
	var greatness = $('#greatness').val();
	if (greatness.length > 100){
		$('#message-container-greatness').append('<span>Come on now, it ain\'t that great (use 100 characters or less).</span>');
		error = true;
	}
	
	var description = $('#description').val();
	if (description.length > 500){
		$('#message-container-description').append('<span>Dude!  This isn\'t a dissertation (use 500 characters or less).</span>');
		error = true;
	}
	
	if (error){
		return false;
	}
	
	return true;
}

function disableAddButton(){
	$('#add-button').prop('value', 'Adding...');
	$('#add-button').prop('disabled', true);
}

function enableAddButton(){
	$('#add-button').prop('value', 'Add it!');
	$('#add-button').prop('disabled', false);
}

function hideAddButton(){
	$('#add-button').hide();
}

function showAddButton(){
	$('#add-button').show();
}

function clearMessages(){
	$('#message-container-idea').empty();
	$('#message-container-idea-date').empty();
	$('#message-container-steal-status').empty();
	$('#message-container-steal-status-description').empty();
	$('#message-container-greatness').empty();
	$('#message-container-description').empty();
}