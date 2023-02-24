
  SELECT *
    FROM BOARD
     LIMIT 10 , 10; 
    
  SELECT COUNT(1) AS CNT
    FROM BOARD; 
    
    SELECT *
      FROM 
			(
			  SELECT *, ROW_NUMBER() OVER(ORDER BY BOARD_ID) AS TTT
				FROM BOARD
			) T
    WHERE TTT BETWEEN 10 AND 20
    ; 
    
   select * from boards.board
    where `board_title` like '%제목1%';
    
    select * from boards.board
    where board_content like "%afdssdafsaf%";
    
    select * from boards.board
    where board_writer like "%작성자2121%";
    
    select * from boards.board
    where board_title like "%제목1%" and board_content like "%내용1%";


   select * from boards.board
   where ( 
            ('1' = '4' and board_title   like '%HI%')
	     or ('2' = '4' and board_content like '%HI%')
	     or ('3' = '4' and board_writer  like '%HI%')
	     or ('4' = '4' and 
              (
                 board_title   like '%HI%'
			  or board_content like '%내용1%'
              ) 
            )
         )
   ;