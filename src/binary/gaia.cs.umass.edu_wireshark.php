<!DOCTYPE HTML>
<html>
  <head>
    <title>Jim Kurose Homepage</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://stackpath.bootstrapcdn.com/bootswatch/4.5.0/lux/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="custom.css" rel="stylesheet" type="text/css"/>
  </head>
  <body>
    <!-- Required scripts for bootstrap to function -->
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script type="text/javascript" src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="script.js"></script>
    <!-- This is the navbar -->
    	<div id='navbar' class="container fixed-top">
		<nav class="navbar navbar-expand-lg mx-auto mt-2 navbar-light py-2 bg-danger">
			<button class="navbar-toggler mb-1" type="button" data-toggle="collapse" data-target="#navbar-toggle" aria-label="toggle">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbar-toggle">
				<ul class="navbar-nav w-100">
					<li class="nav-item">
						<a href="index.php" class="btn btn-outline-primary mt-1 mb-1 ml-2">Home</a>
					</li>
					<li class="nav-item">
						<a href="about.php" class="btn btn-outline-primary mt-1 mb-1 ml-2">About</a>
					</li>
					<li class="nav-item dropdown">
						<a href="#" id='navbarDropdown1' role='button' class="nav-link btn btn-outline-primary dropdown-toggle mt-1 mb-1 ml-2" data-toggle='dropdown'>
							Resources (for everyone)
						</a>
						<div class='dropdown-menu' aria-labelledby='navbarDropdown1'>
							<a class='dropdown-item' href='lectures.php'>Online Video Lectures</a>
							<a class='dropdown-item' href='ppt.php'>Powerpoint</a>
							<a class='dropdown-item' href='wireshark.php'>Wireshark Labs</a>
							<a class='dropdown-item' href='knowledgechecks/'>Knowledge Checks</a>
							<a class='dropdown-item' href='interactive/'>Interactive End-of-Chapter Problems</a>
									<a class='dropdown-item' href='programming.php'>Programming Assignments</a>
						</div>
					</li>
					<li class="nav-item">
						<a href="instructor.php" class="btn btn-outline-primary mt-1 mb-1 ml-2">Instructor Resources</a>
					</li>
					<li class="nav-item dropdown">
						<a href="#" id='navbarDropdown2' role='button' class="nav-link btn btn-outline-primary dropdown-toggle mt-1 mb-1 ml-2" data-toggle='dropdown'>
							More
						</a>
						<div class='dropdown-menu' aria-labelledby='navbarDropdown2'>
							<a class='dropdown-item' href='authors.php'>Authors</a>
							<a class='dropdown-item' href='contact.php'>Contact</a>
							<a class='dropdown-item' href='publisher.php'>Publisher's Page</a>
						</div>
					</li>
				</ul>
			</div>
		</nav>
		<img src='header_graphic_book_8E_3.jpg' class='img-fluid'/>
	</div>
	<div class='d-none d-lg-block'>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
	</div>
	<div class='d-block d-lg-none'>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
	</div>    <!-- All content / each problem goes in here -->
    <div class="container jumbotron" id="maincontainer">

<h5>Wireshark Labs</h5>
<br/>
<p>
	<em>"Tell me and I forget. Show me and I remember. Involve me and I understand."
	<br/>
	Chinese proverb
	</em>
	<br/>
	<br/>
  One's understanding of network protocols can often be greatly deepened by "seeing protocols in action" and
	by "playing around with protocols" - observing  the sequence of messages exchanges between two protocol entities,
	delving down into the details of protocol operation, and causing protocols to  perform certain actions and then
	observing these actions and their consequences. This can be done in simulated scenarios or in a "real" network environment
	such as the Internet. The Java applets in the textbook Web site take the first approach. In these Wireshark labs,
	we'll take the latter approach. You'll be running various network applications in different scenarios using a computer
	on your desk, at home, or in a lab. You'll observe the network protocols in your computer "in action," interacting and
	exchanging messages with protocol entities executing elsewhere in the Internet. Thus, you and your computer will be
	an integral part  of these "live" labs. You'll observe, and you'll learn, by doing.
	<br/>
	<br/>
	The basic tool for observing the messages exchanged between executing protocol entities is called a packet sniffer.
	As the name suggests, a packet sniffer passively copies ("sniffs") messages being sent from and received by your computer;
	it will also display the contents of the various protocol fields of these captured messages. For these labs, we'll use
	the <a href="http://www.wireshark.org">Wireshark packet sniffer</a>. Wireshark is a free/shareware packet sniffer
	(a follow-on to the earlier Ethereal packet sniffer) that runs on Windows, Linux/Unix, and Mac computers. The Wireshark
	labs below  will allow you to explore many of the Internet most important protocols.
	<br/>
	<br/>
	We're making these Wireshark labs freely available to all (faculty, students, readers). They're available in both
	Word and PDF so you can add, modify, and delete content to suit your needs. They obviously represent a
	<span class='font-weight-bold'><em>lot</em></span> of work on our part. In return for use, we only ask the following:
	<ul>
	  <li> If you use these labs (e.g., in a class) that you mention their source (after all, we'd like people to use our book!)</li>
	  <li> If you post any labs on a www site, that you note that they are adapted from (or perhaps identical to) our labs, and note our copyright of this material.</li>
	</ul>
	Solutions to these Wireshark labs are available <em>for course instructors only</em> from the publisher
	(not from the authors) - see our <a href="instructor.php">instructors' page</a> for information about how
	to get a solution, either standalone or for an LMS.
	<br/>
	<br/>
	The version 8.1 Wireshark labs have been significantly modernized and updated in 2021, and come with new Wireshark
	traces files taken in 2021.&nbsp; Click on the links below to download a Wireshark lab on the given topic.
</p>
<br/>

<table class="table table-striped">
  <thead>
  <tr>
	  <th> Lab topic</th>
	  <th> 8th ed.</th>
	   <th> 8th ed.</th>
	   <th> 7th ed.</th>
  </tr>
  </thead>

<!-- ****** GETTING STARTED ******  -->
  <tr>
    <td>Getting Started</td>
	  <td>8.1 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Intro_v8.1.docx">Word</a>)</td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Intro_v8.0.pdf">PDF</a>,
<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Intro_v8.0.docx">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Intro_v7.0.pdf">PDF</a>,
<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Intro_v7.0.doc">Word</a>)</td>
  </tr>


<!-- ****** HTTP ******  -->
  <tr>
    <td>HTTP</td>
	  <td>8.1 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_HTTP_v8.1.doc">Word</a>)</td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_HTTP_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_HTTP_v8.0.doc">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_HTTP_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_HTTP_v7.0.doc">Word</a>)</td>
  </tr>


<!-- ****** DNS ******  -->
  <tr>
    <td>DNS</td>
	  	  <td>8.1 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_DNS_v8.1.doc">Word</a>)</td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_DNS_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_DNS_v8.0.doc">Word</a>)</td>
    <td> 7.01(<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_DNS_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_DNS_v7.0.doc">Word</a>)</td>
  </tr>


<!-- ****** TCP ******  -->
  <tr>
    <td>TCP</td>
	  	  <td>8.1 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_TCP_v8.1.doc">Word</a>)</td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_TCP_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_TCP_v8.0.doc">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_TCP_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_TCP_v7.0.doc">Word</a>)</td>
  </tr>


<!-- ****** UDP ******  -->
  <tr>
    <td>UDP</td>
	  	  <td>8.1 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_UDP_v8.1.doc">Word</a>)</td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_UDP_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_UDP_v8.0.doc">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_UDP_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_UDP_v7.0.doc">Word</a>)</td>
  </tr>


<!-- ****** IP ******  -->
  <tr>
    <td>IP</td>
	  	  <td>8.1 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_IP_v8.1.doc">Word</a>)</td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_IP_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_IP_v8.0.doc">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_IP_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_IP_v7.0.doc">Word</a>)</td>
  </tr>


<!-- ****** NAT ******  -->
<tr>
    <td>NAT</td>
		  <td>8.1 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_NAT_v8.1.doc">Word</a>)</td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_NAT_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_NAT_v8.0.doc">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_NAT_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_NAT_v7.0.doc">Word</a>)</td>
  </tr>

<!-- ****** DHCP ******  -->
  <tr>
    <td>DHCP</td>
	  <td>8.1 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_DHCP_v8.1.doc">Word</a>)</td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_DHCP_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_DHCP_v8.0.doc">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_DHCP_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_DHCP_v7.0.doc">Word</a>)</td>
  </tr>

<!-- ****** ICMP ******  -->
  <tr>
    <td>ICMP</td>
	  <td></td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_ICMP_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_ICMP_v8.0.doc">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_ICMP_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_ICMP_v7.0.doc">Word</a>)</td>
  </tr>


<!-- ****** Ethernet and ARP ******  -->
  <tr>
    <td>Ethernet and ARP</td>
	  	   <td>8.1 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Ethernet_ARP_v8.1.doc">Word</a>)</td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Ethernet_ARP_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Ethernet_ARP_v8.0.doc">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Ethernet_ARP_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_Ethernet_ARP_v7.0.doc">Word</a>)</td>
  </tr>




<!-- ****** 802.11 WiFi ******  -->
  <tr>
    <td>802.11 WiFi</td>
	   <td></td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_802.11_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_802.11_v8.0.doc">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_802.11_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_802.11_v7.0.doc">Word</a>)</td>
  </tr>


<!-- ****** SSL ******  -->
  <tr>

    <td>TLS (V8.1 is TLS; V8.0/7.0 is SSL)</td>
	   <td>8.1 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_TLS_v8.1.doc">Word</a>)</td>
    <td>8.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_SSL_v8.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_SSL_v8.0.doc">Word</a>)</td>
    <td>7.0 (<a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_SSL_v7.0.pdf">PDF</a>, <a href="http://www-net.cs.umass.edu/wireshark-labs/Wireshark_SSL_v7.0.doc">Word</a>)</td>
  </tr>


<!-- ****** trace files ******  -->
  <tr>
    <td>Trace files(new trace files for 8.1;older trace files for 7, 8.0)</td>
    <td><a href="http://www-net.cs.umass.edu/wireshark-labs/wireshark-traces-8.1.zip">wireshark-traces-8.1.zip</a></td>
	  <td><a href="http://www-net.cs.umass.edu/wireshark-labs/wireshark-traces.zip">wireshark-traces.zip</a></td>
    <td><a href="http://www-net.cs.umass.edu/wireshark-labs/wireshark-traces.zip">wireshark-traces.zip</a></td>
  </tr>
</table>
<br/>
<p>
  These Wireshark labs are copyright 2005-2021, J.F. Kurose, K.W. Ross, All Rights Reserved.
	<br/>
	Last update to labs: Nov. 1, 2021
	<br/>
  <em>Comments welcome: <a href="mailto:kurose@cs.umass.edu">kurose@cs.umass.edu</a></em>
</p>


    <!-- End of the main container that holds the problem -->
    </div>
    <!-- This container holds the copyright and contact info -->
    <div class="container">
      <p>We gratefully acknowledge the programming and problem design work of John Broderick (UMass '21), which has really helped to substantially i2 prove this site.</p>
      <p> Copyright © 2010-2022 J.F. Kurose, K.W. Ross<br/>Comments welcome and appreciated: kurose@cs.umass.edu<br></p>
    </div>
  </body>
</html>
