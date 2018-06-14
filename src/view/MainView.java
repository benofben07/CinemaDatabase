package view;

import view.tablemodels.ScreeningTableModel;
import view.tablemodels.MovieTableModel;
import view.booking.SeatBookingFrame;
import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import logic.ScreeningStateContainer;
import logic.entities.EntityEnum;
import logic.entities.Screening;

public class MainView extends JFrame{
    
    private static final String BASE_PATH = 
            "C:\\_Programok\\pt2_másodszor\\CinemaDataBase\\pics\\";
    private JTable table;
    private final Controller c;
    private JPanel upperPanel;
    private JPanel lowerPanel;
    private JPanel imagePanel;
    private EntityEnum currentEntity;
    private Screening currentScreening;
    
    public MainView(Controller c) {
        
        this.c = c;
        
        setTitle("CinemaCity Enhanced Edition");
        setSize(600, 370);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        createMenus();
    }
    
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file       = new JMenu("File");
        JMenu list       = new JMenu( "List..." );
        
        JMenuItem listMovie     = new JMenuItem("Movies");
        JMenuItem listScreening = new JMenuItem("Screenings");
        
        JMenuItem newScreening  = new JMenuItem("Add screening");
        
        JMenuItem exitGame      = new JMenuItem("Exit");
        
        listMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setup(EntityEnum.MOVIE, "", "");
            }
        });
        listScreening.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setup(EntityEnum.CINEMA_HALL, "", "");
                
            }
        });
        newScreening.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createScreening();
                setup(currentEntity, "", "");
            }
        });
        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        list.add(listMovie);
        list.add(listScreening);
        
        file.add(list);
        file.add(newScreening);
        file.add(exitGame);
        menuBar.add(file);
        
        setJMenuBar(menuBar);
    }
    
    private void createScreening() {
        Object[] possibilities = c.getMovieTitles().toArray(new String[0]);
        String movieTitle = createPopUp(possibilities, "Choose movie!", "Movie");
        if (movieTitle == null) {
            display("Addition unsuccessful!");
            return;
        }
        
        possibilities = c.getHallNames().toArray(new String[0]);
        String hallName = createPopUp(possibilities, "Choose cinemahall!", "Cinema hall");
        if (hallName == null) {
            display("Addition unsuccessful!");
            return;
        }
        
        String begin = timePopUp();
        if (begin == null) {
            display("Addition unsuccessful!");
            return;
        }
        
        if (!c.validateDate(begin)) {
            display("Invalid date! Correct form: \'yyyy-mm-dd hh:mm:ss\'");
            return;
        }
        ScreeningStateContainer state = c.newScreening(movieTitle, hallName, begin);
        display(state.toString());
    }
    
    private void display(String error) {
        JOptionPane.showMessageDialog(this, error);
    }
    
    private String timePopUp() {
        return (String) JOptionPane.showInputDialog(this,"time?");
    }
    
    private String createPopUp(Object[] possibilities, String text, 
            String title) {
        return (String)JOptionPane.showInputDialog(
                    this,
                    text,
                    title,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    possibilities[0]);
        
    }
    
    private void setup(EntityEnum entity, String movieFilter, String hallFilter) {
        currentEntity = entity;
        getContentPane().removeAll();
        setSize(600, 370);
        setFilterPanel(entity);
        setTablePanel();
        createTable(entity, movieFilter, hallFilter);
        if (entity != EntityEnum.MOVIE) {
            addRightClickActionToTable();
        } else {
            addLeftClickActionToTable();
        }
        revalidate();
        repaint();
    }
    
    private void addLeftClickActionToTable() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                if (arg0.getButton() == MouseEvent.BUTTON1){
                    String title = (String) 
                            table.getValueAt(table.getSelectedRow(), 0);
                    displayPicture(c.getMoviePictureByName(title));
                }
            }}); 
    }
    
    private void displayPicture(String filePath) {
        System.out.println(filePath);
        imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(200,370));
        
        BufferedImage myPicture = null;
        try {
        myPicture = ImageIO.read(
                new File(BASE_PATH + filePath));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(lowerPanel, "Poster not found!");
            return;
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        
        
        imagePanel.add(picLabel);
        setSize(800, 370);
        add(imagePanel, BorderLayout.EAST);
    }
    
    private String[] getClickedScreening(int selectedRow) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; ++i) {
            sb.append(table.getValueAt(selectedRow, i).toString())
              .append("###");
        }
        
        String result = sb.toString();
        return result.substring(0, result.length() - 4).split("###");
    }
    
    private void addRightClickActionToTable(){
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem   = new JMenuItem("Delete");
        JMenuItem bookSeat     = new JMenuItem("Book Seat");
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if( JOptionPane.showConfirmDialog(lowerPanel, "Yes?") == 0) {
                    String[] values = getClickedScreening(table.getSelectedRow());
                    boolean success = 
                        c.removeScreening(
                            values[0],
                            values[1],
                            values[2]);
                    JOptionPane.showMessageDialog(lowerPanel, 
                            success? "SUCCESS!": "SEATS ALREADY TAKEN!");
                    setup(currentEntity,"","");
                }
            }
        });
        
        bookSeat.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String[] values = getClickedScreening(table.getSelectedRow());
                Screening s = c.screeningFromRaw(values[0], values[1], values[2]);
                currentScreening = s;
                createSeatWindow(s.getCinemaHall().getNumOfRows(),
                                 s.getCinemaHall().getNumOfCols(),
                                 c.getBookedSeats(s));
            }
        });
        
        
        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = table.rowAtPoint(
                                SwingUtilities.convertPoint(
                                        popupMenu, new Point(0, 0), table));
                        if (rowAtPoint > -1) {
                            table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }
        });  
        popupMenu.add(deleteItem);
        popupMenu.add(bookSeat);
        table.setComponentPopupMenu(popupMenu);
    }
    
    private void createSeatWindow(int row, int col, List bookedSeats) {
        new SeatBookingFrame(this, row, col, bookedSeats).setVisible(true);
    }
    
    
    public void bookSeat(List<Pair<Integer, Integer>> seats) {
        seats.forEach((p) -> {
            c.addSeat(currentScreening, p.getKey(), p.getValue());
        });
        setup(currentEntity, "", "");
    }
    
    private void createTable(EntityEnum entity, String movieFilter, String hallFilter) {
        // MOVIE table
        if (entity == EntityEnum.MOVIE) {
            if ( movieFilter.equals("") ) {
                table = new JTable(new MovieTableModel(c.listMovies()));
            } else {
                table = new JTable(new MovieTableModel(c.filterMoviesByTitle(movieFilter)));
            }
        // SCREENING table
        } else {
            // no moviefilter
            if ( movieFilter.equals("") ) {
                // no hallfilter
                if (hallFilter.equals("")) {
                    table = new JTable(new ScreeningTableModel(c.listScreening()));
                // hallfilter
                } else {
                    table = new JTable(new ScreeningTableModel(
                            c.filterScreeningByCinemaHall(hallFilter)));
                }
            // moviefilter    
            } else {
                // no hallfilter
                if (hallFilter.equals("")) {
                    table = new JTable(new ScreeningTableModel(
                            c.filterScreeningByMovie(movieFilter)));
                // hallfilter
                } else {
                    table = new JTable(new ScreeningTableModel(
                                c.filterScreeningByMovieAndCinemaHall(
                                    movieFilter, hallFilter)
                                )
                            );
                }
            }
        }
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true); 
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }
    
    private void setTablePanel() {
        lowerPanel = new JPanel();
        add(lowerPanel, BorderLayout.SOUTH);
    }

    private void setFilterPanel(EntityEnum entity) {
        List<JComboBox> comboBoxes = new ArrayList<>();
        
        upperPanel = new JPanel();
        
        JLabel movieLabel = new JLabel("Movie title:");
        upperPanel.add(movieLabel);
        movieLabel.setPreferredSize( new Dimension(70,30) );

        JComboBox movieCombobox = new JComboBox(getComboArray(c.getMovieTitles()));
        upperPanel.add(movieCombobox);
        comboBoxes.add(movieCombobox);
        
        if (entity != EntityEnum.MOVIE) {
            JLabel hallLabel = new JLabel("Hall name:");
            upperPanel.add(hallLabel);
            hallLabel.setPreferredSize( new Dimension(70,30) );

            JComboBox hallCombobox = new JComboBox(getComboArray(c.getHallNames()));
            upperPanel.add(hallCombobox);
            comboBoxes.add(hallCombobox);
        }
        
        JButton filterButton = new JButton("FILTER!");
        filterButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                filter( comboBoxes );
            } 
        } );
        upperPanel.add(filterButton);
        
        add(upperPanel, BorderLayout.NORTH);
    }
    
    private String[] getComboArray(List list) {
        List<String> movies = new ArrayList<>();
        movies.add("ALL");
        movies.addAll(list);
        return movies.toArray(new String[0]);
    }
    
    private void filter(List<JComboBox> comboBoxes) {
        String movieFilter = comboBoxes.get(0).getSelectedItem().toString();
        String hallFilter  = comboBoxes.size() == 2 ? 
                       comboBoxes.get(1).getSelectedItem().toString() : "";
        if (movieFilter.equals("ALL")) movieFilter = "";
        if (hallFilter.equals("ALL")) hallFilter = "";
        
        setup(currentEntity, movieFilter, hallFilter);
    }

}
