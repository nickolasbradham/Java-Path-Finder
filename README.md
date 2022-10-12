# Java-Path-Finder
This program takes <code>.sim</code> files that are just in txt format. The first line contains four integers split with spaces. The first two are the robot start point. The second two are the robot's destination.
Each line after the first represents a different object's set of keyframes. If there are four lines, there will be four objects. Each line has the repeating format <code>\<t\> \<x\> \<y\></code>. Each set of t, x, and y represent a single keyframe at time t and coordinates (x, y).
See example files for a example formats.

## Interface
![Capture](https://user-images.githubusercontent.com/105989209/195206124-718a7986-13c0-43bd-98c9-c5728c17c061.PNG)
Most of the interface is self explanatory. The black dots are the predefined objects. The grey dot is the robot. The interface allows you to step through the path finding process and through the movement animation.
