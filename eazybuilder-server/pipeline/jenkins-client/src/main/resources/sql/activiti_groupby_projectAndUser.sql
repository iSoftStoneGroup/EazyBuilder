SELECT 
    CONCAT(b.group_name, '/', b.project_name) as pname,
    concat(b.user_name,'/',b.email) as userName,
    SUM(b.pushed) AS push,
    SUM(b.additions) AS lineAdd,
    SUM(b.deletions) AS lineRemove,
    SUM(b.openedmrs) AS opened,
    SUM(b.mergedmrs) AS merged
FROM
    ci_user_statistic b
WHERE
    b.day >= ?
        AND b.day <= ?
GROUP BY CONCAT(b.group_name, '/', b.project_name) , concat(b.user_name,'/',b.email)
order by pname