select * from boards.board;

create table board(
	board_id int not null auto_increment,
    board_title varchar(100),
    board_content varchar(200),
    board_writer varchar(50),
	board_write_date datetime default current_timestamp,
    board_modify_date datetime default current_timestamp ON UPDATE CURRENT_TIMESTAMP,
    primary key(board_id)
);

create table comment(
	comment_sequence INT,
    comment_id INT NOT NULL auto_increment,
    parent_id INT,
	depth INT,
    comment_writer VARCHAR(45),
    comment_content VARCHAR(200),
    comment_write_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    comment_modify_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(comment_id),
    FOREIGN KEY(comment_sequence) references board(board_id)
);

-- delete from boards.comment where comment_id = 3;
--  drop table comment;
select * from boards.comment;
-- drop table board;

-- insert into board(board_title, board_content, board_writer) values("제목1", "내용1", "작성자1");
-- insert into board(board_title, board_content, board_writer) values("제목2", "내용2", "작성자2");
-- insert into board(board_title, board_content, board_writer) values("제목3", "내용3", "작성자3");


-- update board-- 
-- set board_title = '제목3333'
--   , board_content ='내용3333'
-- where board_id = 1;