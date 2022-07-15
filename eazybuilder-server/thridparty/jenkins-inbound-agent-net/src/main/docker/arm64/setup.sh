#!/bin/bash
# ----------------------------------------------------------------------
# name:         setup.sh
# createTime:   2019-01-22
# description:  Add obsutil as custom command for linux.
# requirement:  1.Please set a script parameter,
#                 must be the path of obsutil,
#                 the path can be either absolute path or relative path. 
#               2.Execute with root. 
#                 eg: source setup.sh /home/obsutil
# ----------------------------------------------------------------------
function main()
{
    if [[ -z $1 || -d $1 ]];then
        echo 'no expected param that the path of obsutil!'
        return 1
    fi
     echo '1'
    util_home='/obsutil'
    if [[ ! -d ${util_home} ]]; then
        mkdir ${util_home} || return $?
        chmod 755 ${util_home} || return $?
        echo "make directory ${util_home}, and give it all permissions."
    fi
    perm=$(ls -ld ${util_home}|awk '{print $1}')
    if [[ ${perm} != 'drwxr-xr-x.' ]];then
        chmod 755 ${util_home} || return $?
        echo "give ${util_home} all permissions."
    fi
    
    obsutil_dir=$(cd $(dirname $1); pwd)
    file_name=$(echo $1 | awk -F "/" '{print $NF}')
    obsutil_full_path=$obsutil_dir/$file_name
    obsutil_home_path=${util_home}/${file_name}
    if [[ -f ${obsutil_home_path} || -d ${obsutil_home_path} ]]; then
        rm -rf ${obsutil_home_path} || return $?
    fi
    cp $obsutil_full_path ${util_home} || return $?
    chmod 711 ${obsutil_home_path} || return $?
    echo "copy ${obsutil_full_path} to ${util_home} and give it all permissions."
    
    profile='/etc/profile'
    path=$(echo $PATH)
    cont_count=$(grep -c :${util_home} ${profile})
    if [[ ${cont_count} -eq '0' ]]; then
        p='export PATH=$PATH:/obsutil'
        echo $p >>${profile} || return $?
        source ${profile}
    elif [[ ${path} =~ ":${util_home}:" ]]; then
        echo "the PATH ${util_home} already be seted."
    else
        source ${profile}
    fi
    
    obsutil help
    if [ $? == 0 ];then
        echo "set up successfully！${util_home}"
        return 0
    fi
    return 1
}
main $1
