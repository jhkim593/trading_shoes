package jpa.project.repository;

//import javax.persistence.EntityManager;
//
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//@Commit
//class BoardRepositoryTest {
//    @Autowired
//    private BoardRepository boardRepository;
//
//    @Autowired
//    private EntityManager em;

//    @BeforeEach
//    public void boardInit() {
//        Board board = new Board();
//        board.setContent("1");
//        board.setTitle("1");
//
//        Board board1 = new Board();
//        board1.setContent("2");
//        board1.setTitle("2");
//
//        Board board2 = new Board();
//        board2.setContent("3");
//        board2.setTitle("3");
//
//        Board board3 = new Board();
//        board3.setContent("4");
//        board3.setTitle("4");
//
//        Board board4 = new Board();
//        board4.setContent("5");
//        board4.setTitle("5");
//
//        Board board5 = new Board();
//        board5.setContent("6");
//        board5.setTitle("6");
//
//        em.persist(board);
//        em.persist(board1);
//        em.persist(board2);
//        em.persist(board3);
//        em.persist(board4);
//        em.persist(board5);
//    }

//        @Test
//        public void search()throws Exception{
//
//           Member member=new Member();
//           member.setUsername("1");
//           em.persist(member);
//
//           Board board=new Board();
//           board.setTitle("1");
//           board.addMember(member);
//           board.setContent("1");
//           em.persist(board);
//
//           Board board1=new Board();
//           board1.setTitle("2");
//           board1.setContent("2");
//           board1.addMember(member);
//
//           em.persist(board1);
//
//
//            BoardSearch boardSearch= new BoardSearch();
////            boardSearch.setContent("1");
////            boardSearch.setTitle("1");
//
//            Board_liked board_liked=new Board_liked(board,member);
//            em.persist(board_liked);
//
//            PageRequest pageRequest=PageRequest.of(0,3);
//
////            Page<BoardDto> search = boardRepository.search(boardSearch, pageRequest);
////
////            assertThat(search).extracting("boardLikeCount").containsExactly("1");
//
//            List<BoardDto> boardDtos = boardRepository.searchTest(boardSearch);
//            for (BoardDto boardDto : boardDtos) {
//                System.out.println(boardDto);
//            }
////            assertThat(boardDtos).extracting("boardLikeCount").containsExactly(1,0);
//
//        }
//
//    }

