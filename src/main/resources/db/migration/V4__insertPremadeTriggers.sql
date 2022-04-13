#Hard coded IDs for presentation purposes
insert into messages(message_id, title, text)
values (123, "Channel leave", "User {0} left the channel!");

insert into messages(message_id, title, text)
values (124, "Channel join", "Welcome to the channel {0} !");

insert into messages(message_id, title, text)
values (125, "Channel join", "Hi there,  {0} !");

insert into triggers(trigger_id, channel, is_active, message_id, trigger_event)
values (1, "savijanje-viljusaka", true, 123, 0);

insert into triggers(trigger_id, channel, is_active, message_id, trigger_event)
values (2, "savijanje-viljusaka", true, 124, 1);

insert into triggers(trigger_id, channel, is_active, message_id, trigger_event)
values (3, "savijanje-viljusaka", true, 125, 2);