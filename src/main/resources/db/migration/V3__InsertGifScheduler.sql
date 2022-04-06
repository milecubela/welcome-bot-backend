insert into messages(message_id, title, text, created_at)
values (1108, "gif scheduler", "This message is made for sending scheduled gifs", LOCALTIME);

insert into schedules(schedule_id, is_repeat, is_active, scheduler_interval, created_at, message_id, channel, next_run)
values (1108, 1, 0, 1, LOCALTIME, 1, "C037FSVSEJX", LOCALTIME);