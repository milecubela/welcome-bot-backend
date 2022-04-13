insert into messages(message_id, title, text, created_at)
values (1108, "gif scheduler", "This message is made for sending scheduled gifs", LOCALTIME);

insert into schedules(schedule_id, is_repeat, is_active, scheduler_interval, created_at, message_id, channel, next_run)
values (1108, 1, 1, 1, LOCALTIME, 1108, "C03B9BN2Q3X", LOCALTIME);