# 	QUICK SORT VISUALISATION


<p align="center">
  <a href="#description">Description</a> •
  <a href="#used-technologies">Used technologies</a> •
  <a href="#how-to-use">How To Use</a> •
</p>

![Screenshot of a comment on a GitHub issue showing an image, added in the Markdown, of an Octocat smiling and raising a tentacle.](screen.png)

## Description 
After the user enters the number N **(1 ≤ N ≤ 1000)**, a set of random numbers from the range **[1, 1000]** is formed. Each random number is displayed as text on a button. These buttons are used for visualizing the sorting process. 
Each iteration can be represented as:

<table>
  <tr>
    <th>Marker</th>
    <th>Description</th>
  </tr>
  <tr>
    <td><img src="pivot.png" alt="Image 1" width="100"></td>
    <td valign="center">current <b>pivot</b> number</td>
  </tr>
  <tr>
    <td><img src="move-left.png" alt="Image 2" width="100"></td>
    <td valign="center">move to the left</td>
  </tr>
  <tr>
    <td><img src="move-right.png" alt="Image 3" width="100"></td>
    <td valign="center">move to the right</td>
  </tr>
  <tr>
    <td><img src="swap.png" alt="Image 4" width="100"></td>
    <td valign="center">swap</td>
  </tr>
  <tr>
    <td><img src="sorted.png" alt="Image 5" width="100"></td>
    <td valign="center">sorted</td>
  </tr>
</table>


By pressing the Sort button, the sorting begins, and you can see the visualization of this process.

By pressing the number button for a number that is less than or equal to 30, a new set of numbers and buttons will be formed.


## Used technologies
- Java 17
- Maven 3.8.8
- Swing

## How to use

- Copy the project.
- The project can be launched either without parameters or with a single parameter:  
**java -jar qsv.jar [arg]**, where arg</b> is delay (in ms.) for visualisation. The default value of the delay is 500 ms.   
- Enjoy!
