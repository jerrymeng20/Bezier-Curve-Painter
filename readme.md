# Bezier Curve Painter Ver. 1.0.1
(c) 2021 Yanchen Meng (y58meng). <br>
Last revised: March 19, 2021.

## Introduction
The main area of this painter is a canvas where users can draw Bezier curves. Users can select a pen tool from the list on the side and then use their mouse to draw that a Bezier curve on the canvas. Users can also choose a selection tool to edit the shape of a curve by changing its segment points (i.e., points on the curve) and control points. Additionally, users can specify the colour, line thickness, and style of a selected curve and these properties of a new curve to draw.

## Layouts and Features
The drawing program will have the following layout and features:
* A **menu** bar containing the following menus and menu items:
  * File:
    * **New**: create a new blank drawing.
    * **Load**: load a drawing that the user previously saved (as a .bc file) and allow the user to continue editing the drawing.
    * **Save**: save the current drawing as a .bc (bCurve) file.
    * **Quit**: exit the application (prompt the user to save before exiting).
    * <em>Note: If the user has modified the curves, but attempts to quit/new/load without saving, the painter will prompt the user to save before taking these actions.</em>
  * Help:
    * **About**: display a dialogbox with basic painter introduction, with a image tutorial introducing the basic functionalities.
* A **tool palette** on the left-hand side, with the following tools:
  * A **pen tool** allows the user to enter a curve drawing mode. To draw a curve, the user should first click this tool, then drag the mouse to define the control points associated to the segment points. After that, the user may click and drag somewhere again to define the end point of this curve segment (which is also the start point of the next curve segment), and keep clicking somewhere to define the start/end point for following curve segments.
The segment points and control points of the curve will be shown with a small square. To exit the drawing mode, the user should press ESC on the keyboard.
  * A **selection tool** allows the user to select a curve that has been drawn and enter a curve editing mode. To select a curve, the user should click this tool, then click on an existing curve, resulting in appear of all segment and control points to indicate that its been selected. The user could then drag these segment and control points with their mouse to change the shape of the curve.
Selecting a curve will cause the colour palette, line thickness and style to update their state to reflect the currently selected curve (e.g. if the user selects a red curve with the largest line thickness, the colour palette should change to red, and the line thickness should change to the largest line to match the selected curve). Changing colour, line thickness, or line style when a curve is selected will update the curve to the new values.
Pressing ESC on the keyboard will clear curve selection.
  * A **point type tool** allows the user to change the type of any segment point between smooth and sharp. Once a curve is selected so that the segment and control points reveal, the user could click any segment point on the curve to switch its type between smooth (displayed as WHITE squares) and sharp (displayed as YELLOW squares). When drawing a curve, the point type are set to smooth by default.
  * An **erase tool** allows the user to remove a curve. To do so, the user should click on this tool and then click on a curve to remove it. Pressing DEL while a curve is selected should also delete that selected curve.
* A **line colour icon** that graphically displays the currently selected line colour. Clicking on this icon will bring up a color chooser dialog that lets the user pick a color. This colour will be used as the line colour for any new curves that are drawn.
* A **line thickness palette**: a graphical display of a width slider that the user can select. The user can set the width of curves anywhere between 1.0 and 5.0. Selecting a line width will set the line thickness for any new curves drawn.
* A **line style palette**: a graphical display of solid and dotted lines with four choices. Selecting a line style will set the line style for any new curves that are drawn.

## Image Overview
![tutorial image](./src/main/resources/tutorial.png?raw=true)

## Functionalities
* **Advanced drawing**: The painter grants the ability to set the control points while drawing the curve segments. To do so, the user should first click somewhere to set the position of the segment point, then drag the mouse to set the control point(s) associated with this segment point, and release the mouse to complete drawing the point. Similarly, the user could keep clicking and dragging to define subsequent segment points with their control points.
* **Curve editing**: The user should be able to select any curve and edit its shape by dragging any of the segment and control points with the mouse, as well as changing the type (smooth or sharp) of any segment points. When a curve is selected, the drawing properties of the interface should also change to reflect the properties of the curve.
* **Property editing**: Changing the drawing properties (e.g., colour, line style) of a selected curve should change that curve's properties.
* **Window resizing**: The painter supports dynamic resizing of the application window, so that the application adapts to different window sizes and resolutions. If the user changes the size of the window, the canvas should scale to the size of the window, but the shapes should stay at the same size and position. The painter window has a minimum size of 960 * 600, and maximum size of 1920 * 1080.

## Copyright
(c) 2021 Yanchen Meng, All rights reserved.
