<?php
  include "workdir/config.php";

  //BEGIN DIR CHECK
  if ( !isset($_GET['dir']) )
  {
    $subpad = "./";
  }
  else 
  {
    $dir = trim($_GET['dir']);
    $subpad = "$dir/";
    $correct = true;
    if ($dir == "")
    {
      $subpad = "./";
      $correct = false;
    }
    if ( (strlen($dir) > 0) AND (($dir[0] == ".") OR ($dir[0] == "/")) OR (is_numeric(strpos($dir, '..' ))) )
    {
      $subpad  = "./";
      $correct = false;
    }
    $outputsubpad = "&dir=$dir";
  }
  //END DIR CHECK

  //BEGIN CREATE SESSION
  $uploadsession = false;
  if (isset($_GET['upload']) AND !isset($_POST['upload']))
  { 
    if (isset($_GET['dir']))
    {
      $uploaddir = $_GET['dir'];
    }
    $outputuploadform  = "<FORM METHOD=\"POST\" ACTION=\"index.php?upload=admin$outputsubpad\" ENCTYPE=\"multipart/form-data\">";
    $outputuploadform .= "$uploadfiletext<INPUT TYPE=\"file\" NAME=\"uploadfile\">&nbsp;&nbsp;&nbsp;";
    $outputuploadform .= "$uploadpasswordtext<INPUT TYPE=\"password\" NAME=\"password\">&nbsp;&nbsp;&nbsp;";
    $outputuploadform .= "<INPUT TYPE=\"submit\" VALUE=\"$uploadfiletext2\">";
    $outputuploadform .= "</FORM><br>";
    $uploadsession = true;
  }
  //END CREATE SESSION

  //BEGIN UPLOAD FILE
  if(isset($_POST['password']))
  { 
    $uploadcomplete     = true;
    $passcorrect	= false;
    $passentered  	= $_POST['password'];
    $directorytocopyto  = $subpad;
    if($_POST['password'] == $password)
    {
      $passcorrect = true;
    }
    if ($passcorrect == true)
    {
      if (isset($HTTP_POST_FILES['uploadfile']))
      {
        if (is_uploaded_file($HTTP_POST_FILES['uploadfile']['tmp_name'])) 
        {
          $res = copy($HTTP_POST_FILES['uploadfile']['tmp_name'], $directorytocopyto . $HTTP_POST_FILES['uploadfile']['name']);
          if (!$res) 
          { 
            $uploadcomplete = false; 
          } 
        }
      }
    }
  }
  //END UPLOAD FILE

  //BEGIN DELETE SESSION
  $deletesession = false;
  if (isset($_GET['delete']))
  { 
    $deletesession = true;
  }
  //END DELETE SESSION

  //BEGIN DELETE FILE
  function delDir($dirName) 
  {
    if(empty($dirName)) 
    {
      return true;
    }
    if(file_exists($dirName)) 
    {
      $dir = dir($dirName);
      while($file = $dir->read()) 
      {
        if($file != '.' && $file != '..') 
        {
          if(is_dir($dirName.'/'.$file)) 
          {
            delDir($dirName.'/'.$file);
          } 
          else 
          {
            @unlink($dirName.'/'.$file) or die('File '.$dirName.'/'.$file.' couldn\'t be deleted!');
          }
        }
      }
      $dir->close();
      @rmdir($dirName) or die('Folder '.$dirName.' couldn\'t be deleted!');
    } 
    else 
    {
      return false;
    }
    return true;
  } 

  if(isset($_POST['delpassword']))
  {
    $passcorrect 		= false;
    $filetodelete		= $_POST['choosefile'];
    if($_POST['delpassword'] == $password)    
    {
      $passcorrect = true;
    }
    if ($passcorrect == true)
    {
      if (is_dir("$subpad$filetodelete"))
      {
         delDir("$subpad$filetodelete");
      }
      else
      { 
        unlink("$subpad$filetodelete");        
      }       
    }    
  }
  //END DELETE FILE

  //BEGIN CREATE DIRECTORY
  $createdirsession = false;
  if (isset($_GET['createdir']))
  {
    $outputcreatedir    = "<FORM METHOD=POST ACTION=\"index.php?createdir=admin$outputsubpad\">";
    $outputcreatedir   .= "$newdirectory&nbsp;&nbsp;&nbsp;";
    $outputcreatedir   .= "<INPUT TYPE=\"text\" NAME=\"directoryname\">&nbsp;&nbsp;&nbsp;";
    $outputcreatedir   .= "$uploadpasswordtext<INPUT TYPE=\"password\" NAME=\"createpassword\">&nbsp;&nbsp;&nbsp;";
    $outputcreatedir   .= "<INPUT TYPE=\"submit\" VALUE=\"$newdirectorycreate\">";
    $outputcreatedir   .= "</FORM><br>";
    $createdirsession   = true;
  }
  //END CREATE DIRECTORY
 
  //BEGIN ACTUAL DIRECTORY CREATION
  if(isset($_POST['directoryname']))
  { 
    $createdircomplete     = true;
    $passcorrect           = false;
    $passentered  	   = $_POST['createpassword'];
    if($_POST['createpassword'] == $password)
    {
      $passcorrect = true;
    }
    if ($passcorrect == true)
    { 
      $directoryexists = false;
      $createpath = $subpad . $_POST['directoryname'];
      if (file_exists("$createpath"))
      {
        $directoryexists = true;
      } 
      else
      {
        mkdir("$createpath");
      }          
    }
  }

?>
<HTML>
<HEAD>
<TITLE><?php echo $title; ?></TITLE>
<link rel="stylesheet" type="text/css" href="workdir/style.css">
</HEAD>
<BODY>

<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%"
   <tr>
	<td class="colorbar" height="55">
	<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
	   <tr>
	       <td valign="top" width="250"><img src="workdir/logo.png"></td>
               <td><font color="white" size="2"><?php echo $currently; ?><b>
<?php 
  if($subpad == "./")
  {
    echo $rootdir; 
  }
  else
  {
    echo $rootdir . $subpad; 
  }
?>
</b></font></td>
            </tr>
        </table>

        </td>
   </tr>
   <tr>
	<td class="colorbar2" height="5"><img src="workdir/spacer.htm" height="5" width="1"></td>
   </tr>
   <tr>
	<td class="maincontent" height="*" valign="top">





<p></p>
<table border="0" align="left" width="700" >
<tr>
  <td bgcolor="#F4F4F4"><center><b><?php echo $filename; ?></b></center></td>
  <td bgcolor="#F4F4F4"><center><b><?php echo $type; ?></b></center></td>
  <td bgcolor="#F4F4F4"><center><b><?php echo $size; ?></b></center></td>
  <td bgcolor="#F4F4F4"><center><b><?php echo $filedir ?></b></center></td>
</tr>
<tr>
  <td bgcolor="#F4F4F4"><center><b><a href="index.php?sort=filename&type=asc<?php echo $outputsubpad; ?>"><img src="workdir/up.png" border="0"></a> | <a href="index.php?sort=filename&type=desc<?php echo $outputsubpad; ?>"><img src="workdir/down.png" border="0"></a></b></center></td>
  <td bgcolor="#F4F4F4"><center><b><a href="index.php?sort=type&type=asc<?php echo $outputsubpad; ?>"><img src="workdir/up.png" border="0"></a> | <a href="index.php?sort=type&type=desc<?php echo $outputsubpad; ?>"><img src="workdir/down.png" border="0"></a></b></center></td>
  <td bgcolor="#F4F4F4"><center><b><a href="index.php?sort=size&type=asc<?php echo $outputsubpad; ?>"><img src="workdir/up.png" border="0"></a> | <a href="index.php?sort=size&type=desc<?php echo $outputsubpad; ?>"><img src="workdir/down.png" border="0"></a></b></center></td>
  <td bgcolor="#F4F4F4"><center><b><a href="index.php?sort=filedir&type=asc<?php echo $outputsubpad; ?>"><img src="workdir/up.png" border="0"></a> | <a href="index.php?sort=filedir&type=desc<?php echo $outputsubpad; ?>"><img src="workdir/down.png" border="0"></a></b></center></td>
</tr>
<?php
  function size_translate($filesize)
  {
     $array = array(
         'YB' => 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024,
         'ZB' => 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024,
         'EB' => 1024 * 1024 * 1024 * 1024 * 1024 * 1024,
         'PB' => 1024 * 1024 * 1024 * 1024 * 1024,
         'TB' => 1024 * 1024 * 1024 * 1024,
         'GB' => 1024 * 1024 * 1024,
         'MB' => 1024 * 1024,
         'KB' => 1024,
     );
     if($filesize <= 1024)
     {
         $filesize = $filesize . ' Bytes';
     }
     foreach($array AS $name => $size)
     {
         if($filesize > $size || $filesize == $size)
         {
             $filesize = round((round($filesize / $size * 100) / 100), 2) . ' ' . $name;
         }
     }
     return $filesize;
  }
    if (($dp = opendir("$subpad")) != false)
    {
      $list = array();
      while (($file = readdir($dp)) != false)
      {
        if ($file[0] != ".")
        {
          array_push($list, $file);
        } 
      }
      closedir($dp); 
      sort($list);
      echo "<ul>";
      $totalsize = 0;
      $index = 0; 
      foreach ($list as $file)
      {
        if (($file <> "index.php") AND ($file <> "workdir"))
        {
          $ext = strtolower("." . substr(strrchr($file, "."), 1));
          $subsize = filesize("$subpad$file");
          $totalsize = $totalsize + $subsize;
          $bestandarray[$index]['filename'] = "$file";
          $bestandarray[$index]['type'] = "$ext";
          $bestandarray[$index]['size'] = "$subsize";
          if (is_dir("$subpad$file")) { $filediroutput = $filedir_dir; } else { $filediroutput = $filedir_file; }
          $bestandarray[$index]['filedir'] = "$filediroutput";
          $index++;
        }
      }     
      
      if( count($bestandarray) > 0 )
      {
        if (isset($_GET['sort']))
        {
          $sort = $_GET['sort'];
          $sorttype = $_GET['type'];
          if (($sort == "filename") AND ($sorttype == "asc"))
          {
            foreach($bestandarray as $res) $sortAux[] = $res['filename'];
            array_multisort($sortAux, SORT_ASC, $bestandarray);
            echo "<b>$sorted</b>$sortfilename - $sortasc<br><br>";
          }
          else if (($sort == "filename") AND ($sorttype == "desc"))
          {
            foreach($bestandarray as $res) $sortAux[] = $res['filename'];
            array_multisort($sortAux, SORT_DESC, $bestandarray);
            echo "<b>$sorted</b>$sortfilename - $sortdesc<br><br>";
          }
          else if (($sort == "type") AND ($sorttype == "asc"))
          {
            foreach($bestandarray as $res) $sortAux[] = $res['type'];
            array_multisort($sortAux, SORT_ASC, $bestandarray);
            echo "<b>$sorted</b>$sorttypefile - $sortasc<br><br>";
          }
          else if (($sort == "type") AND ($sorttype == "desc"))
          {
            foreach($bestandarray as $res) $sortAux[] = $res['type'];
            array_multisort($sortAux, SORT_DESC, $bestandarray);
            echo "<b>$sorted</b>$sorttypefile - $sortdesc<br><br>";
          }
          else if (($sort == "size") AND ($sorttype == "asc"))
          {
            foreach($bestandarray as $res) $sortAux[] = $res['size'];
            array_multisort($sortAux, SORT_ASC, $bestandarray);
            echo "<b>$sorted</b>$sortsize - $sortasc<br><br>";
          }
          else if (($sort == "size") AND ($sorttype == "desc"))
          {
            foreach($bestandarray as $res) $sortAux[] = $res['size'];
            array_multisort($sortAux, SORT_DESC, $bestandarray);
            echo "<b>$sorted</b>$sortsize - $sortdesc<br><br>";
          }
          else if (($sort == "filedir") AND ($sorttype == "asc"))
          {
            foreach($bestandarray as $res) $sortAux[] = $res['filedir'];
            array_multisort($sortAux, SORT_ASC, $bestandarray);
            echo "<b>$sorted</b>$sortfiledir - $sortasc<br><br>";
          }
          else if (($sort == "filedir") AND ($sorttype == "desc"))
          {
            foreach($bestandarray as $res) $sortAux[] = $res['filedir'];
            array_multisort($sortAux, SORT_DESC, $bestandarray);
            echo "<b>$sorted</b>$sortfiledir - $sortdesc<br><br>";
          }
          else
          {
            foreach($bestandarray as $res) $sortAux[] = $res['filedir'];
            array_multisort($sortAux, SORT_ASC, $bestandarray);
            echo "<b>$welcomemessage</b><br><br>";
          }
       }
       else
       {
         foreach($bestandarray as $res) $sortAux[] = $res['filedir'];
         array_multisort($sortAux, SORT_ASC, $bestandarray);
         echo "<b>$welcomemessage</b><br><br>";
       }
    }
    else 
    {
      echo "<b>$welcomemessage</b><br><br>$nofiles<br><br>";
    }      

    if ($uploadsession == true)
    {
      echo $outputuploadform;
    }

    if ($createdirsession == true)
    {
      echo $outputcreatedir;
    }

    if (isset($_POST['password']))
    {
      if ($passcorrect == false)
      {
        echo "$passnotcorrect<br><br>";
      }
      else if ($uploadcomplete == true)
      {
        echo "$uploadfilefinished<br><br>";
      }
      else
      {
        echo "$uploadfilenotfinished<br><br>";
      }
    }
  
    if($deletesession == true AND count($bestandarray) > 0)
    {
      echo "<form method=post action=index.php?delete=admin$outputsubpad>";
      echo "<select name=\"choosefile\">";
      for ($i=0; $i < count($bestandarray); $i++)
      {
        $outputfilenametext = $bestandarray[$i]['filename'];
        echo "<option value=\"$outputfilenametext\">$outputfilenametext";
      } 
      echo "</select>";
      echo "&nbsp;&nbsp;&nbsp;$uploadpasswordtext<INPUT TYPE=\"password\" name=\"delpassword\">&nbsp;&nbsp;&nbsp;<INPUT TYPE=\"submit\" VALUE=\"Delete File\">";
      echo "</form><br>";
    }

    if(isset($_POST['delpassword']))
    {
      if ($passcorrect == false)
      {
        echo "<br>$passnotcorrect<br><br>";
      }
      else
      {
        echo "<br>$deletefinished<br><br>";  
      }
    }

    if(isset($_POST['createpassword']))
    {
      if ($passcorrect == false)
      {
        echo "<br>$passnotcorrect<br><br>";
      }
      else if ($directoryexists == true)
      {
        echo "$newdirectoryexistsalready<br><br>";  
      }
      else
      {
        echo "$newdirectorycreated<br><br>";
      }
    }




      //CHECK
      if (isset($_GET['dir']) AND ($correct == true))
      {
        $directory = $_GET['dir'];
        $slashes = 0;
        $positie = 0;
        for ($i=0; $i < strlen($directory); $i++)
        {
          if ($directory[$i] == "/")
          {
            $slashes++;
            $positie = $i;
          }
        }
        if ($slashes == 0)
        {
          $directory = "./";
        }
        else
        {
          $directory = substr($directory, 0, $positie);
        }
        echo "<a href=\"index.php?dir=$directory\">$levelup</a><br><br>";
      }
      //END CHECK

      $countarray = count($bestandarray);
      for($i = 0; $i < $countarray; $i++)
      {
        $outputfilename =  $bestandarray[$i]['filename'];
        $outputfiletype =  $bestandarray[$i]['type'];
        $outputfilesize =  $bestandarray[$i]['size'];
        $outputfilesize =  size_translate($outputfilesize);
        $outputfiledir  =  $bestandarray[$i]['filedir']; 
     
        echo "<tr>";
        if(is_dir("$subpad$outputfilename")) 
        {
          if($subpad <> "./")
          {
             echo "<td bgcolor=#F9F9F9><center><a href=\"index.php?dir=$dir/$outputfilename\">$outputfilename</a></center></td>";             
          }
          else
          {
             echo "<td bgcolor=#F9F9F9><center><a href=\"index.php?dir=$outputfilename\">$outputfilename</a></center></td>";                        
          }
        }
        else 
        {
             echo "<td bgcolor=#F9F9F9><center><a href=\"$rootdir$subpad$outputfilename\" target=\"_blank\">$outputfilename</a></center></td>";
        }
        echo "<td bgcolor=#F9F9F9><center>$outputfiletype</center></td>";
        echo "<td bgcolor=#F9F9F9><center>$outputfilesize</center></td>";
        echo "<td bgcolor=#F9F9F9><center>$outputfiledir</center></td>";
        echo "</tr>";
      }
          

        $totalsize = size_translate($totalsize);
        echo "<tr>";
        echo "<td bgcolor=#FFFFFF></td>";
        echo "<td bgcolor=#FFFFFF></td>";
        echo "<td bgcolor=#F4F4F4><center><b>$totalsize</b></center></td>";
        echo "<td bgcolor=#FFFFFF></td>";
        echo "</tr>";
    }
?>     
</table>


	</td>

   </tr>

   <tr>
	<td class="hrz_line" height="1"><img src="workdir/spacer.htm" height="1" width="1"></td>
   </tr>
   <tr>
	<td class="foot" align="right" height="50"><?php echo $created; ?><a href="http://www.dutchville.com" target="_new">DutchVille.com</a>. Copyright © 2005. [ <acronym title="<?php echo $uploadtext; ?>"><a href="index.php?upload=admin<?php echo $outputsubpad; ?>" border="0"><?php echo $uploadlink; ?></a> | </acronym><acronym title="<?php echo $deletetext; ?>"><a href="index.php?delete=admin<?php echo $outputsubpad; ?>" border="0"><?php echo $deletelink; ?></a></acronym> | </acronym><acronym title="<?php echo $directorytext ?>"><a href="index.php?createdir=admin<?php echo $outputsubpad; ?>" border="0"><?php echo $directorylink; ?></a></acronym>]</td>
   </tr>
</table>

</body></html>