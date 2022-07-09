select 
    CONCAT(b.group_name, '/', b.project_name) as pname,
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
GROUP BY CONCAT(b.group_name, '/', b.project_name) 
order by push desc