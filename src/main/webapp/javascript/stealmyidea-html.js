/*
 <div style="border-bottom: thin solid black;">
				<p>Idea: 3DTV</p>
				<p>Date: 1998</p>
				<p>Has it been stolen: Apparently ... only took 22 years ... <a href="https://lookingglassfactory.com/">https://lookingglassfactory.com/</a></p>
				<p>Greatness: off the charts</p>
				<p>A tv could be a cube and you could walk around it and see stuff from different angles.  Kind of like a hologram in a box.</p>
			</div>
 */

function createMessagesHtml(messages){
	
	var messagesHtml = '<div>';
	
	for (var index = 0; index < messages.length; index++){
		var message = messages[index];
		var messageHtml = createMessageHtml(message);
		
		messagesHtml = messagesHtml + messageHtml;
	}
	
	messagesHtml = messagesHtml + '</div>';
	
	return messgaesHtml;
}

function createMessageHtml(message){
	
	var messageHtml = '<div>' + message + '</div>';
	
	return messageHtml;
}

function createIdeasHtml(ideas){
	
	var ideasHtml = '<div id="ideas-container" class="ideas-container">';
	
	for (var index = 0; index < ideas.length; index++){
		var idea = ideas[index];
		
		var ideaHtml = createIdeaHtml(idea);
		
		ideasHtml = ideasHtml + ideaHtml;
	}
	
	ideasHtml = ideasHtml + '</div>';
	
	return ideasHtml;
}

function createIdeaHtml(idea){

	/*
	 '<div id="idea-steal-status-container-' + idea.ideaId + '" class="idea-steal-status-container">' + 
							'<span class="idea-steal-status-label">Has it been stolen?</span>' +
							'<span class="idea-steal-status-content">' + nullToEmpty(idea.stealStatus) + '</span>' + 
						'</div>' +
	 */
	
	var ideaHtml = '<div id="idea-container-' + idea.ideaId + '" class="idea-container">' + 
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