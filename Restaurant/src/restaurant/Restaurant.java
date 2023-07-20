package restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Restaurant extends javax.swing.JPanel {

    List<Object[]> dataList; // List to store restaurant data
    private String selectedSortBy = "Default";; // Default value, change it to the initial value of jComboBox1

    public static void main(String[] args) {

        // Create a JFrame to hold the Restaurant panel
        javax.swing.JFrame frame = new javax.swing.JFrame("Restaurant Application");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        // Create an instance of the Restaurant panel
        Restaurant restaurantPanel = new Restaurant();

        // Add the Restaurant panel to the JFrame
        frame.getContentPane().add(restaurantPanel);

        // Set the size and make the JFrame visible
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Creates new form Restaurant
     */
    public Restaurant() {
        initComponents();
        dataList = new ArrayList<>(); // Initialize the dataList
        displayDataInTable();
        populateRestaurantInfoTable();
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://cctmcagenda2.mysql.database.azure.com:3306/restaurant"; // Modify the URL accordingly
        String username = "alif"; // Default username for XAMPP
        String password = "alep1234!"; // Empty password by default in XAMPP
        return DriverManager.getConnection(url, username, password);
    }

    private void displayDataInTable() {
    dataList.clear(); // Clear the existing data in the global dataList

    try (Connection connection = getConnection();
         Statement statement = connection.createStatement()) {
        String query = "SELECT * FROM detail"; // Change the query according to your database schema
        ResultSet resultSet = statement.executeQuery(query);

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear any existing rows in the table

        // Use lambda expression to process the ResultSet and add data to the dataList
        dataList = resultSetToArrayList(resultSet);

        // Add the row data to the table model
        dataList.forEach(model::addRow);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    private List<Object[]> resultSetToArrayList(ResultSet resultSet) throws SQLException {
    List<Object[]> dataList = new ArrayList<>();
    while (resultSet.next()) {
        // Retrieve data from the result set
        int recordid = resultSet.getInt("recordid");
        String restaurantName = resultSet.getString("name");
        String location = resultSet.getString("location");
        String cuisine = resultSet.getString("cuisine");
        int rating = resultSet.getInt("rating");
        String review = resultSet.getString("review");

        // Create an array to store the row data
        Object[] rowData = {recordid, restaurantName, location, cuisine, rating, review};

        // Add the row data to the global dataList
        dataList.add(rowData);
    }
    return dataList;
}

    private void populateRestaurantInfoTable() {
        updateRestaurantInfoTable();
    }

    private boolean validateFields() {
        String restaurantName = jTextField1.getText().trim();
        String location = jTextField2.getText().trim();
        String cuisine = jTextField3.getText().trim();
        String ratingStr = jTextField4.getText().trim();
        String review = jTextField10.getText().trim();

        if (restaurantName.isEmpty() || location.isEmpty() || cuisine.isEmpty() || ratingStr.isEmpty() || review.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 10) {
                JOptionPane.showMessageDialog(this, "Rating must be between 1 and 10.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Rating must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;

    }

    private boolean validateFields2() {
        String restaurantName = jTextField6.getText().trim();
        String location = jTextField7.getText().trim();
        String cuisine = jTextField8.getText().trim();
        String ratingStr = jTextField9.getText().trim();
        String review = jTextField11.getText().trim();

        if (restaurantName.isEmpty() || location.isEmpty() || cuisine.isEmpty() || ratingStr.isEmpty() || review.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 10) {
                JOptionPane.showMessageDialog(this, "Rating must be between 1 and 10.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Rating must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;

    }

    private void insertData(Connection connection, int restaurantId, String restaurantName, String location, String cuisine, int rating, String review) throws SQLException {
        String query = "INSERT INTO detail (restaurantid, name, location, cuisine, rating, review) VALUES (?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, restaurantId);
            preparedStatement.setString(2, restaurantName);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, cuisine);
            preparedStatement.setInt(5, rating);
            preparedStatement.setString(6, review);
            preparedStatement.executeUpdate();
        }
    }

    private int findLargestRestaurantId(Connection connection) throws SQLException {
        String query = "SELECT MAX(restaurantid) AS max_id FROM detail";
        try ( Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("max_id");
            }
            return 0; // If no records found in the table, start with 0 as restaurantId
        }
    }

    private int findExistingDetailId(Connection connection, String restaurantName) throws SQLException {
        String query = "SELECT restaurantid FROM detail WHERE name = ?";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, restaurantName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int restaurantId = resultSet.getInt("restaurantid");
                resultSet.close(); // Close the ResultSet after retrieving the data
                return restaurantId;
            }
            resultSet.close(); // Close the ResultSet if no data found
            return -1; // Restaurant name does not exist in the database
        }
    }

    private void resetFields() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField10.setText("");
    }

    private void resetFields2() {
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField11.setText("");
    }

private void sortTableByColumn(JTable table, String sortBy) {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    table.setRowSorter(sorter);

    int columnIndex = getColumnIndexByName(table, sortBy);
    if (columnIndex == -1) {
        JOptionPane.showMessageDialog(this, "Invalid column name.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    List<RowSorter.SortKey> sortKeys = new ArrayList<>();
    sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING));

    sorter.setComparator(columnIndex, (o1, o2) -> {
        // Use lambda expression for sorting based on column data type
        if (o1 instanceof String && o2 instanceof String) {
            return ((String) o1).compareToIgnoreCase((String) o2);
        } else if (o1 instanceof Integer && o2 instanceof Integer) {
            return Integer.compare((Integer) o1, (Integer) o2);
        } else if (o1 instanceof Float && o2 instanceof Float) {
            return Float.compare((Float) o1, (Float) o2);
        } else if (o1 instanceof Double && o2 instanceof Double) {
            return Double.compare((Double) o1, (Double) o2);
        }
        return 0;
    });

    sorter.setSortKeys(sortKeys);
    sorter.sort();
}
   // Method to sort jTable2 by the selected column name
private void sortTable2ByColumn(String columnName) {
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) jTable2.getModel());
    jTable2.setRowSorter(sorter);

    int columnIndex = getColumnIndexByName(jTable2, columnName);
    if (columnIndex == -1) {
        JOptionPane.showMessageDialog(this, "Invalid column name.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    List<RowSorter.SortKey> sortKeys = new ArrayList<>();
    sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING));

    sorter.setSortKeys(sortKeys);
    sorter.sort();
}

    // Method to get the column index by column name
private int getColumnIndexByName(JTable table, String columnName) {
    for (int i = 0; i < table.getColumnCount(); i++) {
        if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
            return i;
        }
    }
    return -1; // Column with the given name not found
}

    // Helper method to check if a string is a valid integer
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void updateDataListFromDatabase() {
        dataList.clear(); // Clear the existing data in the list

        try ( Connection connection = getConnection()) {
            String query = "SELECT * FROM detail";
            try ( PreparedStatement statement = connection.prepareStatement(query);  ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int recordid = resultSet.getInt("recordid");
                    String restaurantName = resultSet.getString("name");
                    String location = resultSet.getString("location");
                    String cuisine = resultSet.getString("cuisine");
                    int rating = resultSet.getInt("rating");
                    String review = resultSet.getString("review");
                    Object[] rowData = {recordid, restaurantName, location, cuisine, rating, review};
                    dataList.add(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDataInDatabase(int recordId, String restaurantName, String location, String cuisine, int rating, String review) {
        try {
            String query = "UPDATE detail SET Name=?, Location=?, Cuisine=?, Rating=?, Review=? WHERE recordid=?";
            Connection conn = getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, restaurantName);
            pst.setString(2, location);
            pst.setString(3, cuisine);
            pst.setInt(4, rating);
            pst.setString(5, review);
            pst.setInt(6, recordId);

            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   private void updateRestaurantInfoTable() {
    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    model.setRowCount(0); // Clear any existing rows in the table

    try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
        String query = "SELECT restaurantid, name, rating FROM detail"; // Specify only the necessary columns
        ResultSet resultSet = statement.executeQuery(query);

        Map<String, List<Integer>> restaurantRatingsMap = new HashMap<>(); // Map to store restaurant ratings (restaurant name as key)

        while (resultSet.next()) {
            int restaurantId = resultSet.getInt("restaurantid");
            String restaurantName = resultSet.getString("name");
            int rating = resultSet.getInt("rating");

            // Use lambda expression to update the ratings for each restaurant in the map
            restaurantRatingsMap.computeIfAbsent(restaurantName, k -> new ArrayList<>()).add(rating);
        }

        for (Map.Entry<String, List<Integer>> entry : restaurantRatingsMap.entrySet()) {
            String restaurantName = entry.getKey();
            List<Integer> ratings = entry.getValue();

            int reviewCount = ratings.size();
            double averageRating = ratings.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);

            Object[] rowData = {findExistingDetailId(connection, restaurantName), restaurantName, reviewCount, averageRating};
            model.addRow(rowData);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
   }
   


    private void deleteDataFromDatabase(int recordId) {
        try ( Connection conn = getConnection();  PreparedStatement pst = conn.prepareStatement("DELETE FROM detail WHERE recordid = ?")) {
            pst.setInt(1, recordId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        label1 = new java.awt.Label();
        panel1 = new java.awt.Panel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        label3 = new java.awt.Label();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        label4 = new java.awt.Label();
        jComboBox2 = new javax.swing.JComboBox<>();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        label1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label1.setText("Saved Restaurant Data:");

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel1.setText("Add Data Part");

        jLabel2.setText("Restaurant Name :");

        jLabel3.setText("Location :");

        jLabel4.setText("Cuisine :");

        jLabel5.setText("Rating :");

        jTextField4.setToolTipText("");

        jButton1.setText("Submit Review");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Search by Record Id:");

        jButton2.setText("search");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Reset");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel7.setText("Restaurant Name :");

        jLabel8.setText("Location :");

        jLabel9.setText("Cuisine :");

        jLabel10.setText("Review :");

        jButton4.setText("Update");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Delete");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Reset");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel11.setText("Review :");

        jLabel12.setText("Rating :");

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(jLabel1))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addGap(30, 30, 30)
                                    .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6)
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel8)
                                                .addComponent(jLabel7)
                                                .addComponent(jLabel9))
                                            .addGap(2, 2, 2))
                                        .addComponent(jLabel11)
                                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel3))))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel10)))
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField7)
                            .addComponent(jTextField8)
                            .addComponent(jTextField9)
                            .addComponent(jTextField6)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(jButton4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton5)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton6))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField1)
                                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                                            .addComponent(jTextField4)
                                            .addComponent(jTextField2)
                                            .addComponent(jTextField5)
                                            .addComponent(jTextField10))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton2)))
                                .addGap(0, 6, Short.MAX_VALUE))
                            .addComponent(jTextField11))))
                .addGap(29, 29, 29))
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(33, 33, 33)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addGap(40, 40, 40)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(20, 20, 20)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addGap(19, 19, 19))
        );

        label3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        label3.setText("Sort By :");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "RecordID", "Restaurant Name", "Location", "Cuisine", "Rating", "Review" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "RecordId", "Restaurant Name", "Location", "Cuisine", "Rating", "Review"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Restaurant ID", "Restaurant Name", "Number of Review", "Average Rating"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable2);

        jLabel13.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel13.setText("Restaurant Rating");

        label4.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        label4.setText("Sort By :");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Restaurant ID ", "Restaurant Name", "Number of Review", "Average Rating" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(178, 178, 178))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(214, 214, 214)
                                .addComponent(jLabel13))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(182, 182, 182)
                                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155)
                        .addComponent(jLabel13)
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 370, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (validateFields()) {
            String restaurantName = jTextField1.getText().trim();
            String location = jTextField2.getText().trim();
            String cuisine = jTextField3.getText().trim();
            int rating = Integer.parseInt(jTextField4.getText().trim());
            String review = jTextField10.getText().trim();

            try ( Connection connection = getConnection()) {
                int existingRestaurantId = findExistingDetailId(connection, restaurantName);

                if (existingRestaurantId != -1) {
                    // Restaurant name already exists, use the existing restaurantId
                    insertData(connection, existingRestaurantId, restaurantName, location, cuisine, rating, review);
                } else {
                    int largestRestaurantId = findLargestRestaurantId(connection);
                    int newRestaurantId = largestRestaurantId + 1;
                    insertData(connection, newRestaurantId, restaurantName, location, cuisine, rating, review);
                }

                // After inserting the data, update the dataList with the new record
                updateDataListFromDatabase();

                // Display the updated data in the table
                displayDataInTable();
                populateRestaurantInfoTable();
                resetFields();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred while saving data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String searchRecordIdText = jTextField5.getText().trim();

        if (searchRecordIdText.isEmpty() || !isInteger(searchRecordIdText)) {
            // Show a message to ask the user to insert a valid recordid
            JOptionPane.showMessageDialog(this, "Please enter a valid RecordId (a positive integer).", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rowIndex = -1;

        // Search for the rowIndex that contains the given recordId in jTable1
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String value = jTable1.getValueAt(i, 0).toString(); // Convert to string
            if (value.equals(searchRecordIdText)) {
                rowIndex = i;
                break;
            }
        }

        if (rowIndex != -1) {
            // RecordId found, display the data in the JTextFields
            jTextField6.setText((String) jTable1.getValueAt(rowIndex, 1)); // Restaurant Name
            jTextField7.setText((String) jTable1.getValueAt(rowIndex, 2)); // Location
            jTextField8.setText((String) jTable1.getValueAt(rowIndex, 3)); // Cuisine
            jTextField9.setText(String.valueOf(jTable1.getValueAt(rowIndex, 4))); // Rating
            jTextField11.setText((String) jTable1.getValueAt(rowIndex, 5)); // Review
        } else {
            // RecordId not found, show a message
            JOptionPane.showMessageDialog(this, "RecordId not found.", "RecordId Not Found", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        resetFields();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int selectedRowIndex = jTable1.getSelectedRow();

        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateFields2()) {
            return; // Validation failed, do not proceed with the update
        }

        String restaurantName = jTextField6.getText().trim();
        String location = jTextField7.getText().trim();
        String cuisine = jTextField8.getText().trim();
        String ratingStr = jTextField9.getText().trim();
        String review = jTextField11.getText().trim();

        // Validate the rating field to ensure it contains a valid integer
        if (!ratingStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer value for the Rating field.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method without proceeding with the update
        }

        int rating = Integer.parseInt(ratingStr);

        // Update the data in the database
        String recordIdStr = jTable1.getValueAt(selectedRowIndex, 0).toString(); // Get the recordId as a String from the selected row
        int recordId = Integer.parseInt(recordIdStr); // Parse the recordId String to an int

        updateDataInDatabase(recordId, restaurantName, location, cuisine, rating, review);

        // Refresh the dataList with the latest data from the database
        updateDataListFromDatabase();

        // Update jTable1 with the latest data
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear any existing rows in the table

        for (Object[] rowData : dataList) {
            model.addRow(rowData);
        }

        // Refresh jTable2 with the latest restaurant information
        updateRestaurantInfoTable();

        // Clear the input fields
        resetFields2();

        // Show a success message
        JOptionPane.showMessageDialog(this, "Data updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int selectedRowIndex = jTable1.getSelectedRow();

        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            // Get the recordId from the selected row
            String recordIdStr = jTable1.getValueAt(selectedRowIndex, 0).toString();
            int recordId = Integer.parseInt(recordIdStr);

            // Delete the data from the database
            deleteDataFromDatabase(recordId);

            // Refresh jTable1 with the latest data
            updateDataListFromDatabase();
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Clear any existing rows in the table
            for (Object[] rowData : dataList) {
                model.addRow(rowData);
            }

            // Refresh jTable2 with the latest restaurant information
            updateRestaurantInfoTable();

            // Show a success message
            JOptionPane.showMessageDialog(this, "Data deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        resetFields2();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

// TODO add your handling code here:
        // Inside initComponents() method
       
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
           String newSelectedSortBy = (String) jComboBox1.getSelectedItem();

    // Only perform sorting and show the message if the selected sorting item is different from the current one
    if (!newSelectedSortBy.equals(selectedSortBy)) {
        // Update the selected sorting item
        selectedSortBy = newSelectedSortBy;

        // Check if "Default" is selected, and display a message to inform the user
        if (selectedSortBy.equals("Default")) {
            JOptionPane.showMessageDialog(Restaurant.this, "Please select a sorting item from the dropdown.", "Select Sorting Item", JOptionPane.WARNING_MESSAGE);
            return; // Exit the method without sorting the table
        }

        // Sort the table based on the selected item
        sortTableByColumn(jTable1, selectedSortBy);
    }
            }
        });

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int selectedRow = jTable1.getSelectedRow();

        // Ensure that the user actually clicked on a row and not outside the table
        if (selectedRow >= 0) {
            // Check the number of columns in the jTable1 to avoid ArrayIndexOutOfBoundsException
            int columnCount = jTable1.getColumnCount();
            if (columnCount >= 6) { // Make sure the jTable1 has at least 6 columns
                // Get data from the selected row
                String restaurantName = jTable1.getValueAt(selectedRow, 1).toString();
                String location = jTable1.getValueAt(selectedRow, 2).toString();
                String cuisine = jTable1.getValueAt(selectedRow, 3).toString();
                String rating = jTable1.getValueAt(selectedRow, 4).toString();
                String review = jTable1.getValueAt(selectedRow, 5).toString();

                // Update JTextFields with the selected row data
                jTextField6.setText(restaurantName);
                jTextField7.setText(location);
                jTextField8.setText(cuisine);
                jTextField9.setText(rating);
                jTextField11.setText(review);
            } else {
                // Handle the case when the jTable1 doesn't have enough columns
                JOptionPane.showMessageDialog(this, "The selected row data cannot be displayed correctly.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


    }//GEN-LAST:event_jTable1MouseClicked

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Default", "Restaurant ID", "Restaurant Name", "Number of Review", "Average Rating"}));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String newSelectedSortBy = (String) jComboBox2.getSelectedItem();

                // Only perform sorting and show the message if the selected sorting item is different from the current one
                if (!newSelectedSortBy.equals(selectedSortBy)) {
                    // Update the selected sorting item
                    selectedSortBy = newSelectedSortBy;

                    // Check if "Default" is selected, and display a message to inform the user
                    if (selectedSortBy.equals("Default")) {
                        JOptionPane.showMessageDialog(Restaurant.this, "Please select a sorting item from the dropdown.", "Select Sorting Item", JOptionPane.WARNING_MESSAGE);
                        return; // Exit the method without sorting the table
                    }

                    // Sort jTable2 based on the selected item
                    sortTable2ByColumn(selectedSortBy);
                } else {

                }

            }
        });
    }//GEN-LAST:event_jComboBox2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private java.awt.Label label1;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Panel panel1;
    // End of variables declaration//GEN-END:variables
}
