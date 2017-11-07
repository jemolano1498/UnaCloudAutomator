#!bin/bash 
 sudo apt-get install -y curl 
 #Download Chef installer 
 sudo curl -L https://www.opscode.com/chef/install.sh | bash 
 #Create chef node.json 
 echo '{  "run_list": [ "recipe[blastDB]"  ]}' > /etc/chef/node.json 
 #Define chef paths 
 echo 'file_cache_path "/etc/chef" 
 	 cookbook_path "/etc/chef/cookbooks" 
 	 json_attribs "/etc/chef/node.json"' > /etc/chef/solo.rb 
 #Directory for DB recipe  
 mkdir -p /etc/chef/cookbooks/blastDB/recipes 
 #Blast DB recipe 
 echo '%w[ /home/user/ncbi-blast-2.6.0+ /home/user/ncbi-blast-2.6.0+/blast /home/user/ncbi-blast-2.6.0+/blast/db ].each do |path| 
 	 directory path do 
 	 owner "user" 
	 group "user" 
	 mode "0755" 
	 end 
 end 
 ENV["BLASTDB"] = "/home/user/ncbi-blast-2.6.0+/blast/db/" 
 Environment="BLASTDB=/home/user/ncbi-blast-2.6.0+/blast/db/" 
 #Protein Metagenomes DB 01 
 %w[ 00 01 ].each do |index| 
 	remote_file "/home/user/ncbi-blast-2.6.0+/blast/db/env_nr.#{index}.tar.gz" do 
 	  source "ftp://ftp.ncbi.nlm.nih.gov/blast/db/env_nr.#{index}.tar.gz" 
 	  owner "user" 
 	  group "user" 
 	  mode "0755" 
 	  action :create 
 	end 
  
 	execute "/home/user/ncbi-blast-2.6.0+/blast/db/" do 
 	  command "tar xzvf /home/user/ncbi-blast-2.6.0+/blast/db/env_nr.#{index}.tar.gz -C /home/user/ncbi-blast-2.6.0+/blast/db/" 
 	end 
  
 	file "/home/user/ncbi-blast-2.6.0+/blast/db/env_nr.#{index}.tar.gz" do 
 	  action :delete 
 	end 
 end 
  ' > /etc/chef/cookbooks/blastDB/recipes/default.rb 
 
 #Execute chef configuration 
 sudo chef-solo -c /etc/chef/solo.rb