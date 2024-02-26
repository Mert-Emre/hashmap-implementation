To run the program compile all files. Then run the command:<br>
java project2 <initial_file> <input_file> <outputfile> <br>
We are the CEO of a restaurant chain and we need a program to help us manage. There are 4 types of workers: <br>

<ul>
  <li>Courier</li>
  <li>Cashier</li>
  <li>Cook</li>
  <li>Manager</li>
</ul>
Couriers do not get promotion. Cashiers can promote to a cook and cooks can promote to a manager. An employee can start in any position. To promote from cashier to cook 3 promotion poinst should be collected and to promote from cook to manager 10 promotion points should be collected. Workers get promotions during month as dollars.If the monthly score of a worker is positive, the worker gets 1 promotion points for each 200 dollars promotion he/she has. If it is negative then the worker loses one promotion points for each 200 dollars. Excess of the promotion is given as extra wage. If a cashier has 3 promotion poinst but there is only one cashier in this restaurant, the cashier can not be promoted. When a new cashier is recruited if he/she still has more than 3 points, than he/she is promoted to cook. Same applies to the cooks.<br>
If an employee collects -5 promotion points, they will be dismissed. There are some
dismissal rules:<br>
If a manager’s promotion point drops to -5, the cooks are checked:<br>
<ul>
  <li>If there is only one cook, the manager cannot be dismissed.</li>
  <li>If there is at least one cook who reaches or exceeds 10 points and the total
  number of cooks is more than 1, the manager is dismissed and the cook who
  have reached 10 points first is promoted to manager.</li>
  <li>If there is no cook who have reached 10 points, the manager cannot be dismissed
  that month.</li>
  <li>If the manager gains some points and their points become larger than -5 in
  the next month, the manager continues to work.</li>
  <li>• If the manager cannot get rid of -5 points, the same routine as the previous
month should be applied.</li>
</ul>
<ul>
<li> If a cook is tried to be dismissed and there is only 1 cook, the cook cannot be
dismissed.</li>
<li>If there is only 1 cashier, the cashier cannot be dismissed.</li>
<li>If there is only 1 courier, the courier cannot be dismissed.</li>
<li>If there is only one courier or cook or cashier in the branch and this person wants to
leave, the person is not allowed to leave the job. If the person who wants to leave is
the manager, the process in the dismissal section is followed.</li>
<li>Same rules for being dismissed is applied to leaving. The only difference is that leaving
is the choice of the employee, therefore it will be given. However, employees are
dismissed according to their promotion points, regardless of their preferences.</li>
</ul>
