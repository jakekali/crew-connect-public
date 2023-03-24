// Imports

import React from 'react';

// Just fetching the list of chats

const ChatList = ({chats, onSelect}) => {

    // Return chat list HTML

    return (
        <div className='page'>
            <h3>Your Chats</h3>
                {chats.map((chat) => (
                    <button key={chat.id} onClick={() => onSelect(chat.id)}>
                        {chat.name}
                    </button>
                    
                ))}
        </div>
    );
};

export default ChatList;