<h2>🎯 Objective</h2>

The layout this experiement intends to replicate is this: 

<img width="818" alt="image (3)" src="https://github.com/user-attachments/assets/a12acfba-b56f-45d0-a594-41610ffe0bd2">

The design requirement to place items in a grid like view with a fixed number of columns and a height that matches the size of the screen. All items have a fixed width but variable heights. 

<h2>🖼️ Measurement and placement rules:</h2> 

If an item completely fits in a column, then it should be placed in that column with a header, items and a footer component. If the item does not completely fit a specified column, it should be partitioned such that, if the header and 
at least one item fits in the current column, then the header should be displayed with as many items that fit into the current column with a cut out decorator at end of the current column and at the beginning of the next. Otherwise, the item needs to be placed on the next column (or columns following the aforementioned rule). 

Note that the header and each item has variable heights. The footer has a fixed height. The layout should have a horizontal scroll and items that are not currently visible in the viewport should be loaded lazily. 

The objective of this layout is to maximize the screen real estate and place a large quantity of items in the available screen space.
